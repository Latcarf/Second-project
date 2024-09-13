package RoyalHouse.controller.user;

import RoyalHouse.model.Request;
import RoyalHouse.service.admin.main.RequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main")
public class MainRequestController {
    private final RequestService requestService;

    public MainRequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/new-request")
    public String getRequestForm(Model model) {
        model.addAttribute("request", new Request());
        return "user/request-form";
    }

    @PostMapping("/new-request")
    public String createRequest(@ModelAttribute Request request) {
        requestService.createRequest(request);
        return "redirect:/main";
    }


}