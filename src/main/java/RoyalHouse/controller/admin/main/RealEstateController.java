package RoyalHouse.controller.admin.main;

import RoyalHouse.model.Building.RealEstate;
import RoyalHouse.service.admin.main.RealEstateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/main/real-estates")
public class RealEstateController {

    private final RealEstateService realEstateService;

    public RealEstateController(RealEstateService realEstateService) {
        this.realEstateService = realEstateService;
    }

    @GetMapping
    public String getRealEstate(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "5") int size,
                                @RequestParam(value = "name", required = false) String name,
                                @RequestParam(value = "type", required = false) String type,
                                @RequestParam(value = "room", required = false) Integer room,
                                Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "date"));
        Page<RealEstate> realEstates = realEstateService.getRealEstates(name, type, room, pageRequest);

        int startPage = Math.max(1, (page - 1) / 10 * 10 + 1);
        int endPage = Math.min(startPage + 9, realEstates.getTotalPages());

        model.addAttribute("realEstates", realEstates);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", realEstates.getTotalPages());
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("name", name);
        model.addAttribute("type", type);
        model.addAttribute("room", room);

        return "admin/main/real-estate/real-estates";
    }

    @GetMapping("/{id}")
    public String getRealEstate(@PathVariable Long id,  Model model) {
        RealEstate realEstates = realEstateService.getRealEstateById(id);
        model.addAttribute("realEstate", realEstates);
        return "admin/main/real-estate/real-estate-details";
    }

}
