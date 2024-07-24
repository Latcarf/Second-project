package RoyalHouse.controller.admin.main;

import RoyalHouse.model.Request;
import RoyalHouse.service.admin.main.RequestService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/main/requests")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("request", new Request());
        return "admin/main/create-request";
    }

    @PostMapping("/new")
    public String createRequest(@ModelAttribute Request request, Model model) {
        try {
            requestService.createRequest(request);
            return "redirect:/admin/main/requests";
        } catch (Exception e) {
            return "admin/main/create-request";
        }
    }

    @GetMapping
    public String getRequests(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "5") int size,
                              @RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "phone", required = false) String phone,
                              @RequestParam(value = "email", required = false) String email,
                              @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                              @RequestParam(value = "status", required = false) String status,
                              Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "date"));
        Page<Request> requests = requestService.getRequests(name, phone, email, date, status, pageRequest);

        int startPage = Math.max(1, (page - 1) / 10 * 10 + 1);
        int endPage = Math.min(startPage + 9, requests.getTotalPages());

        model.addAttribute("requests", requests.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", requests.getTotalPages());
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("name", name);
        model.addAttribute("phone", phone);
        model.addAttribute("email", email);
        model.addAttribute("date", date);
        model.addAttribute("status", status);
        return "/admin/main/request/requests";
    }

    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response) {
    }

    @GetMapping("/{id}")
    public String getRequest(@PathVariable Long id, Model model) {
        Request request = requestService.getRequestById(id);
        model.addAttribute("request", request);
        return "/admin/main/request/request-details";
    }

    @GetMapping("/changeStatus/{id}")
    public String changeStatus(@PathVariable Long id,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "5") int size,
                               @RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "phone", required = false) String phone,
                               @RequestParam(value = "email", required = false) String email,
                               @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                               @RequestParam(value = "status", required = false) String status) {
        requestService.changeStatus(id);

        String dateParam = (date != null) ? date.toString() : "";

        return String.format("redirect:/admin/main/requests?page=%d&size=%d&name=%s&phone=%s&email=%s&date=%s&status=%s",
                page, size, name, phone, email, dateParam, status);
    }




    @PostMapping("/delete/{id}")
    public String deleteRequest(@PathVariable Long id, Model model) {
        try {
            requestService.deleteRequest(id);
            return "redirect:/admin/main/requests";
        } catch (Exception e) {
            model.addAttribute("error", "Delete error: " + e.getMessage());
            return "redirect:/admin/main/requests";
        }
    }
}
