package RoyalHouse.controller.admin.main;

import RoyalHouse.model.building.Address;
import RoyalHouse.model.building.Details;
import RoyalHouse.model.building.RealEstate;
import RoyalHouse.model.Photo;
import RoyalHouse.model.modelEnum.EntityType;
import RoyalHouse.service.PhotoService;
import RoyalHouse.service.admin.main.RealEstateService;
import RoyalHouse.service.admin.main.NewBuildingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/main/real-estates")
public class RealEstateController {

    private final RealEstateService realEstateService;
    private final NewBuildingService newBuildingService;
    private final PhotoService photoService;

    public RealEstateController(RealEstateService realEstateService, NewBuildingService newBuildingService, PhotoService photoService) {
        this.realEstateService = realEstateService;
        this.newBuildingService = newBuildingService;
        this.photoService = photoService;
    }

    @GetMapping
    public String getRealEstates(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "5") int size,
                                 @RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "type", required = false) String type,
                                 @RequestParam(value = "room", required = false) Integer room,
                                 Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "date"));
        Page<RealEstate> realEstates = realEstateService.getRealEstates(name, type, room, pageRequest);

        int startPage = Math.max(1, (page - 1) / 10 * 10 + 1);
        int endPage = Math.min(startPage + 9, realEstates.getTotalPages());

        model.addAttribute("realEstates", realEstates.getContent());
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
    public String getRealEstate(@PathVariable Long id, Model model) {
        RealEstate realEstate = realEstateService.getRealEstateById(id);
        model.addAttribute("realEstate", realEstate);
        return "admin/main/real-estate/real-estate-details";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("realEstate", new RealEstate());
        model.addAttribute("address", new Address());
        model.addAttribute("details", new Details());
        model.addAttribute("newBuildings", newBuildingService.getNewBuildings());
        return "admin/main/real-estate/create-real-estate";
    }

    @PostMapping("/create")
    public String createRealEstate(@ModelAttribute RealEstate realEstate,
                                   @ModelAttribute Address address,
                                   @ModelAttribute Details details,
                                   @RequestParam(value = "newBuildingId", required = false) Long newBuildingId,
                                   @RequestParam("photos") MultipartFile[] photos) {

        List<Photo> photoList = photoService.savePhotos(Arrays.asList(photos), EntityType.REAL_ESTATE, realEstate.getId());
        realEstateService.createRealEstate(realEstate, address, details, newBuildingId, photoList);
        return "redirect:/admin/main/real-estates";
    }

    @PostMapping("/delete/{id}")
    public String deleteRealEstate(@PathVariable Long id,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "5") int size,
                                   @RequestParam(value = "name", required = false) String name,
                                   @RequestParam(value = "type", required = false) String type,
                                   @RequestParam(value = "room", required = false) Integer room) {
        realEstateService.deleteRealEstate(id);

        return String.format("redirect:/admin/main/real-estates?page=%d&size=%d&name=%s&type=%s&room=%s",
                page, size, name, type, room != null ? room.toString() : "");
    }
}
