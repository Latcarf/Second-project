package RoyalHouse.controller.admin.main;

import RoyalHouse.dto.Pagination;
import RoyalHouse.model.company.Service;
import RoyalHouse.service.PhotoService;
import RoyalHouse.service.admin.main.ServiceService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin/main/services")
public class ServiceController {

    private final ServiceService serviceService;
    private final PhotoService photoService;

    public ServiceController(ServiceService serviceService, PhotoService photoService) {
        this.serviceService = serviceService;
        this.photoService = photoService;
    }

    @GetMapping
    public String getService(@RequestParam(defaultValue = "1") int page,
                             @RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "status", required = false) String status,
                             Model model) {
        Map<String, Object> filterParams = new HashMap<>();
        if (Objects.nonNull(name)) filterParams.put("name", name);
        if (Objects.nonNull(status)) filterParams.put("status", status);

        Pagination<Service> pagination = Pagination.create(page, serviceService, filterParams, "date");

        model.addAttribute("services", pagination.getPageData());
        model.addAttribute("pagination", pagination);
        model.addAttribute("filterParams", filterParams);

        return "admin/main/service/services";
    }

    @GetMapping("/changeStatus/{id}")
    public String getChangesStatus(@PathVariable Long id,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(value = "name", required = false) String name,
                                   @RequestParam(value = "status", required = false) String status) {
        serviceService.changeStatus(id);

        return String.format("redirect:/admin/main/services?page=%d&name=%s&status=%s",
                page, name, status);
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("service", new Service());
        return "admin/main/service/create-service";
    }

    @Transactional
    @PostMapping("/create")
    public String createService(@ModelAttribute Service service,
                                @RequestParam("preview") MultipartFile preview,
                                @RequestParam("banner") MultipartFile banner) {

        serviceService.createService(service);

        String photoUrl = photoService.saveServicePhoto(preview, service.getName(), service.getId());
        String bannerUrl = photoService.saveServiceBanner(banner, service.getName(), service.getId());

        service.setPhotoUrl(photoUrl);
        service.setBannerUrl(bannerUrl);

        return "redirect:/admin/main/services";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        model.addAttribute("service", serviceService.getServiceById(id));
        return "/admin/main/service/edit-service";
    }

    @PostMapping("/edit")
    public String updateService(@ModelAttribute Service service,
                                @RequestParam("preview") MultipartFile preview,
                                @RequestParam("banner") MultipartFile banner) {

        serviceService.editService(service, preview, banner);

        return "redirect:/admin/main/services";
    }



    @PostMapping("/delete/{id}")
    public String deleteNewBuilding(@PathVariable Long id,
                                    @RequestParam(defaultValue = "1") int page,
                                    @RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "status", required = false) String status) {
        serviceService.deleteService(id);

        return String.format("redirect:/admin/main/services?page=%d&name=%s&status=%s",
                page, name, status);
    }
}
