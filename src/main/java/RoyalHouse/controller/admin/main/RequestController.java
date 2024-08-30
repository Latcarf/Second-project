package RoyalHouse.controller.admin.main;

import RoyalHouse.dto.Pagination;
import RoyalHouse.model.Request;
import RoyalHouse.service.admin.main.EmailService;
import RoyalHouse.service.admin.main.ExportService;
import RoyalHouse.service.admin.main.RequestService;
import RoyalHouse.service.admin.setting.ContactService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin/main/requests")
public class RequestController {

    private final RequestService requestService;
    private final ExportService exportService;

    public RequestController(RequestService requestService, ExportService exportService) {
        this.requestService = requestService;
        this.exportService = exportService;
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
            model.addAttribute("error", "Failed to create request. Please try again.");
            return "admin/main/create-request";
        }
    }

    @GetMapping
    public String getRequests(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "phone", required = false) String phone,
                              @RequestParam(value = "email", required = false) String email,
                              @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                              @RequestParam(value = "status", required = false) String status,
                              Model model) {
        Map<String, Object> filterParams = new HashMap<>();
        if (Objects.nonNull(name)) filterParams.put("name", name);
        if (Objects.nonNull(phone)) filterParams.put("phone", phone);
        if (Objects.nonNull(email)) filterParams.put("email", email);
        if (Objects.nonNull(date)) filterParams.put("date", date);
        if (Objects.nonNull(status)) filterParams.put("status", status);

        Pagination<Request> pagination = Pagination.create(page, requestService, filterParams);

        model.addAttribute("requests", pagination.getPageData().getContent());
        model.addAttribute("pagination", pagination);
        model.addAttribute("filterParams", filterParams);

        return "admin/main/request/requests";
    }



    @GetMapping("/export")
    public ResponseEntity<ByteArrayResource> exportToExcel(
            @RequestParam Map<String, Object> allParams) {
        try {
            PageRequest pageRequest = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.DESC, "date"));
            Page<Request> requestsPage = requestService.getPage(pageRequest, allParams);
            byte[] excelData = exportService.exportRequestsToExcel(requestsPage.getContent());

            ByteArrayResource resource = new ByteArrayResource(excelData);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=requests.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(excelData.length)
                    .body(resource);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/{id}")
    public String getRequest(@PathVariable Long id, Model model) {
        Request request = requestService.getRequestById(id);
        model.addAttribute("request", request);
        return "admin/main/request/request-details";
    }

    @GetMapping("/changeStatus/{id}")
    public String changeStatus(@PathVariable Long id,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "phone", required = false) String phone,
                               @RequestParam(value = "email", required = false) String email,
                               @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                               @RequestParam(value = "status", required = false) String status) {
        requestService.changeStatus(id);

        String dateParam = (date != null) ? date.toString() : "";

        return String.format("redirect:/admin/main/requests?page=%d&name=%s&phone=%s&email=%s&date=%s&status=%s",
                page, name, phone, email, dateParam, status);
    }

    @PostMapping("/delete/{id}")
    public String deleteRequest(@PathVariable Long id,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(value = "name", required = false) String name,
                                @RequestParam(value = "phone", required = false) String phone,
                                @RequestParam(value = "email", required = false) String email,
                                @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                @RequestParam(value = "status", required = false) String status) {
        requestService.deleteRequest(id);

        String dateParam = (date != null) ? date.toString() : "";

        return String.format("redirect:/admin/main/requests?page=%d&name=%s&phone=%s&email=%s&date=%s&status=%s",
                page, name, phone, email, dateParam, status);
    }
}
