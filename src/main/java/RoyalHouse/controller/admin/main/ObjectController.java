package RoyalHouse.controller.admin.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/main")
public class ObjectController {

    @GetMapping("/objects")
    public String objects() {
        return "admin/main/objects";
    }
}
