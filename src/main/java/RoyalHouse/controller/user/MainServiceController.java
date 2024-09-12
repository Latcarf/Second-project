package RoyalHouse.controller.user;

import RoyalHouse.model.company.Service;
import RoyalHouse.service.admin.setting.ServiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/main")
public class MainServiceController {
    private final ServiceService serviceService;

    public MainServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping("/services")
    String getServices(Model model) {
        List<Service> services = serviceService.getActiveService();
        model.addAttribute("services", services);
        return "user/services";
    }

    @GetMapping("/service-details/{id}")
    String getServiceDetails(@PathVariable Long id, Model model) {
        Service service = serviceService.getServiceById(id);
        model.addAttribute("service", service);
        return "user/service-details";
    }
}
