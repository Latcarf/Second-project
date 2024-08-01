package RoyalHouse.controller.admin.setting;

import RoyalHouse.service.admin.setting.PasswordForm;
import RoyalHouse.model.Contact;
import RoyalHouse.service.admin.setting.ContactService;
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
            @Valid @ModelAttribute PasswordForm passwordForm,
            BindingResult passwordBindingResult,
            Model model) {

        if (Objects.isNull(requestEmails)) {
            requestEmails = List.of();
        }

        if (Objects.nonNull(passwordForm.getNewPassword()) && !passwordForm.getNewPassword().isEmpty() && !passwordForm.getNewPassword().equals(passwordForm.getConfirmPassword())) {
            passwordBindingResult.rejectValue("confirmPassword", "password.mismatch", "New Password and Confirm Password do not match");
        }

        if (!bindingResult.hasErrors() && !passwordBindingResult.hasErrors()) {
            contactService.updateContact(contact, requestEmails, passwordForm.getCurrentPassword(), passwordForm.getNewPassword(), passwordForm.getConfirmPassword(), passwordBindingResult);
        }

        if (bindingResult.hasErrors() || passwordBindingResult.hasErrors()) {
            model.addAttribute("createMode", false);
            model.addAttribute("requestEmails", requestEmails);
            model.addAttribute("passwordForm", passwordForm);
            return "admin/setting/contacts";
        }

        return "redirect:/admin/setting/contacts";
    }
}
