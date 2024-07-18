package RoyalHouse.controller.admin.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/main")
public class ServiceController {

    @GetMapping("/services")
    public String services() {
        return "admin/main/services";
    }
}
