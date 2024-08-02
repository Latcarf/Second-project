package RoyalHouse.controller.admin.setting;

import RoyalHouse.dto.PasswordForm;
import RoyalHouse.model.Contact;
import RoyalHouse.service.admin.setting.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin/setting/contacts")
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactService.class);
    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public String contacts(Model model) {
        Contact contact = contactService.getContact();
        if (Objects.nonNull(contact)) {
            model.addAttribute("contact", contact);
            List<String> requestEmails = contactService.getRequestEmails(contact.getId());
            model.addAttribute("requestEmails", requestEmails);
            model.addAttribute("createMode", false);
            model.addAttribute("passwordForm", new PasswordForm());
        } else {
            model.addAttribute("contact", new Contact());
            model.addAttribute("requestEmails", List.of());
            model.addAttribute("createMode", true);
        }
        return "admin/setting/contacts";
    }

    @PostMapping("/create")
    public String createContact(
            @Valid @ModelAttribute Contact contact,
            BindingResult bindingResult,
            @RequestParam(value = "requestEmails", required = false) List<String> requestEmails,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "confirmPassword") String confirmPassword,
            Model model) {

        if (!password.equals(confirmPassword)) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "Password and Confirm Password do not match");
        }

        if (requestEmails == null) {
            requestEmails = List.of();
        }

        if (!bindingResult.hasErrors()) {
            contactService.createContact(contact, requestEmails, password, bindingResult);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("createMode", true);
            model.addAttribute("requestEmails", requestEmails);
            return "admin/setting/contacts";
        }

        return "redirect:/admin/setting/contacts";
    }

    @PostMapping("/update")
    public String updateContact(
            @Valid @ModelAttribute Contact contact,
            BindingResult bindingResult,
            @RequestParam(value = "requestEmails", required = false) List<String> requestEmails,
            Model model) {

        logger.info("Updating contact with ID: {}", contact.getId());

        if (Objects.isNull(requestEmails)) {
            requestEmails = List.of();
        }

        if (!bindingResult.hasErrors()) {
            contactService.updateContact(contact, requestEmails, bindingResult);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("createMode", false);
            model.addAttribute("contact", contact);
            model.addAttribute("requestEmails", requestEmails);
            model.addAttribute("passwordForm", new PasswordForm());
            return "admin/setting/contacts";
        }

        return "redirect:/admin/setting/contacts";
    }

    @PostMapping("/updatePassword")
    public String updatePassword(
            @ModelAttribute("passwordForm") @Valid PasswordForm passwordForm,
            BindingResult passwordBindingResult,
            Model model) {

        logger.info("Updating password for contact with current password: {}", passwordForm.getCurrentPassword());

        Contact contact = contactService.getContact();

        if (!passwordForm.getNewPassword().equals(passwordForm.getConfirmPassword())) {
            passwordBindingResult.rejectValue("confirmPassword", "password.mismatch", "New Password and Confirm Password do not match");
        }

        if (!passwordBindingResult.hasErrors()) {
            contactService.updatePassword(contact, passwordForm.getCurrentPassword(), passwordForm.getNewPassword(), passwordForm.getConfirmPassword(), passwordBindingResult);
        }

        if (passwordBindingResult.hasErrors()) {
            model.addAttribute("createMode", false);
            model.addAttribute("contact", contact);
            List<String> requestEmails = contactService.getRequestEmails(contact.getId());
            model.addAttribute("requestEmails", requestEmails);
            model.addAttribute("passwordForm", passwordForm);
            return "admin/setting/contacts";
        }

        return "redirect:/admin/setting/contacts";
    }

    @PostMapping("/addRequestEmail")
    public String addRequestEmail(
            @RequestParam("email") String email,
            @RequestParam("contactId") Long contactId,
            BindingResult bindingResult,
            Model model) {

        Contact contact = contactService.findContactById(contactId);
        if (contact == null) {
            bindingResult.rejectValue("contactId", "contact.notfound", "Contact not found.");
        } else {
            contactService.addRequestEmail(contact, email, bindingResult);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("createMode", false);
            model.addAttribute("contact", contact);
            List<String> requestEmails = contactService.getRequestEmails(contact.getId());
            model.addAttribute("requestEmails", requestEmails);
            model.addAttribute("passwordForm", new PasswordForm());
            return "admin/setting/contacts";
        }

        return "redirect:/admin/setting/contacts";
    }

    @PostMapping("/removeRequestEmail")
    public String removeRequestEmail(@RequestParam("contactId") Long contactId, @RequestParam("email") String email) {
        contactService.removeRequestEmail(contactId, email);
        return "redirect:/admin/setting/contacts";
    }

}
