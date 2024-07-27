package RoyalHouse.controller.admin.setting;

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
        } else {
            model.addAttribute("contact", new Contact());
            model.addAttribute("requestEmails", List.of(""));
            model.addAttribute("createMode", true);
        }
        return "admin/setting/contacts";
    }

    @PostMapping("/create")
    public String createContact(
            @Valid @ModelAttribute Contact contact,
            BindingResult bindingResult,
            @RequestParam("requestEmails") List<String> requestEmails,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "confirmPassword") String confirmPassword,
            Model model) {

        if (bindingResult.hasErrors() || !password.equals(confirmPassword)) {
            model.addAttribute("createMode", true);
            model.addAttribute("requestEmails", requestEmails);
            if (!password.equals(confirmPassword)) {
                model.addAttribute("confirmPasswordError", "Password and Confirm Password do not match");
            }
            return "admin/setting/contacts";
        }

        contact.setPassword(password);
        contactService.createContact(contact, requestEmails);

        return "redirect:/admin/setting/contacts";
    }

    @PostMapping("/update")
    public String updateContact(
            @Valid @ModelAttribute Contact contact,
            BindingResult bindingResult,
            @RequestParam("requestEmails") List<String> requestEmails,
            @RequestParam(value = "currentPassword", required = false) String currentPassword,
            @RequestParam(value = "newPassword", required = false) String newPassword,
            @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
            Model model) {

        if (bindingResult.hasErrors() || (newPassword != null && !newPassword.equals(confirmPassword))) {
            model.addAttribute("createMode", false);
            model.addAttribute("requestEmails", requestEmails);
            if (Objects.nonNull(newPassword) && !newPassword.equals(confirmPassword)) {
                model.addAttribute("confirmPasswordError", "New Password and Confirm Password do not match");
            }
            return "admin/setting/contacts";
        }

        if (Objects.nonNull(newPassword) && !newPassword.isEmpty()) {
            contactService.changePassword(contact, currentPassword, newPassword, confirmPassword);
        }

        contactService.updateContact(contact, requestEmails);

        return "redirect:/admin/setting/contacts";
    }

}

