package RoyalHouse.controller;

import RoyalHouse.model.Request;
import RoyalHouse.service.RequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/requests")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("request", new Request());
        return "create-request";
    }

    @PostMapping
    public String createRequest(@ModelAttribute Request request, Model model) {
        try {
            requestService.createRequest(request);
            return "redirect:/requests";
        } catch (Exception e) {
            return "create-request";
        }
    }

    @GetMapping
    public String getAllRequests(Model model) {
        List<Request> requests = requestService.getAllRequest();
        model.addAttribute("requests", requests);
        return "requests-list";
    }

    @GetMapping("/requests/{id}")
    public String getRequests(@PathVariable Long id, Model model) {
        Request request = requestService.getRequestById(id);
        model.addAttribute("request", request);
        return "requests-list";
    }

    @GetMapping("/delete/{id}")
    public String deleteRequest(@PathVariable Long id, Model model) {
        try {
            requestService.deleteRequest(id);
            return "redirect:/requests";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при удалении заявки: " + e.getMessage());
            return "requests-list";
        }
    }
}
