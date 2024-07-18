package RoyalHouse.controller.admin.setting;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/setting")
public class EditServiceController {

    @GetMapping("/edit-services")
    public String editServices() {
        return "admin/setting/edit-services";
    }
}
