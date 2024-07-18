package RoyalHouse.controller.admin.setting;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/setting")
public class SnapObjectController {

    @GetMapping("/snap-objects")
    public String snapObjects() {
        return "admin/setting/snap-objects";
    }
}
