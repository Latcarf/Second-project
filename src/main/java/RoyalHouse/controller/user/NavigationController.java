package RoyalHouse.controller.user;

import RoyalHouse.model.Contact;
import RoyalHouse.service.admin.setting.ContactService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class NavigationController {

    private final ContactService contactService;

    public NavigationController(ContactService contactService) {
        this.contactService = contactService;
    }

    @ModelAttribute("contact")
    public Contact addContactToModel() {
        return contactService.getContact();
    }

    @ModelAttribute("currentURI")
    public String addCurrentUriToModel(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
