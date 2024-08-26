package RoyalHouse.controller.admin.main;

import RoyalHouse.dto.Pagination;
import RoyalHouse.model.building.Address;
import RoyalHouse.model.building.Details;
import RoyalHouse.model.building.RealEstate;
import RoyalHouse.service.PhotoService;
import RoyalHouse.service.admin.main.NewBuildingService;
import RoyalHouse.service.admin.main.RealEstateService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                                 @RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "type", required = false) String type,
                                 @RequestParam(value = "room", required = false) Integer room,
                                 Model model) {

        Map<String, Object> filterParams = new HashMap<>();
        if (name != null) filterParams.put("name", name);
        if (type != null) filterParams.put("type", type);
        if (room != null) filterParams.put("room", room);

        Pagination<RealEstate> pagination = Pagination.create(page, realEstateService, filterParams);

        model.addAttribute("realEstates", pagination.getPageData().getContent());
        model.addAttribute("pagination", pagination);
        model.addAttribute("name", name);
        model.addAttribute("type", type);
        model.addAttribute("room", room);

        return "admin/main/real-estate/real-estates";
    }

    @GetMapping("/{id}")
    public String getRealEstate(@PathVariable Long id, Model model) {
        RealEstate realEstate = realEstateService.getRealEstateById(id);
        List<String> photos = realEstate.getPhotoUrls();
        if (photos == null) {
            photos = new ArrayList<>();
        }
        model.addAttribute("realEstate", realEstate);
        model.addAttribute("photos", photos);
        return "admin/main/real-estate/real-estate-details";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("realEstate", new RealEstate());
        model.addAttribute("address", new Address());
        model.addAttribute("realEstateDetails", new Details());
        model.addAttribute("newBuildings", newBuildingService.getNewBuildings());
        return "admin/main/real-estate/create-real-estate";
    }

    @Transactional
    @PostMapping("/create")
    public String createRealEstate(@ModelAttribute RealEstate realEstate,
                                   @ModelAttribute Address address,
                                   @ModelAttribute Details details,
                                   @RequestParam(value = "newBuildingId", required = false) Long newBuildingId,
                                   @RequestParam("photos") List<MultipartFile> photos) {

        realEstateService.createRealEstate(realEstate, address, details, newBuildingId);

        List<String> photoUrls = photoService.savePhotos(photos, realEstate.getType(), realEstate.getName(), realEstate.getId());

        realEstate.setPhotoUrls(photoUrls);

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
