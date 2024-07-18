package RoyalHouse.controller.admin.setting;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/setting")
public class AboutCompanyController {

    @GetMapping("/about-company")
    public String aboutCompany() {
        return "admin/setting/about-company";
    }
}
