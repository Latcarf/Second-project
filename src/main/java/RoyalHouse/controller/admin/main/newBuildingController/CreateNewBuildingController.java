package RoyalHouse.controller.admin.main.newBuildingController;

import RoyalHouse.model.building.*;
import RoyalHouse.service.PhotoService;
import RoyalHouse.service.admin.main.NewBuildingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.support.SessionStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/main/new-buildings")
@SessionAttributes("newBuilding")
public class CreateNewBuildingController {

    private final NewBuildingService newBuildingService;

    private Map<String, MultipartFile> tempUrl = new HashMap<>();
    private Map<String, List<MultipartFile>> tempUrls = new HashMap<>();

    public CreateNewBuildingController(NewBuildingService newBuildingService) {
        this.newBuildingService = newBuildingService;
    }

    @ModelAttribute("newBuilding")
    public NewBuilding newBuilding() {
        return new NewBuilding();
    }

    @GetMapping("/create-main")
    public String showCreateMain(@ModelAttribute("newBuilding") NewBuilding newBuilding, Model model, HttpServletRequest request) {
        model.addAttribute("newBuildingDetails", newBuilding.getDetails() != null ? newBuilding.getDetails() : new Details());
        model.addAttribute("currentURI", request.getRequestURI());
        return "admin/main/new-building/create/create-main";
    }

    @PostMapping("/create-main")
    public String createMain(@ModelAttribute("newBuilding") NewBuilding newBuilding,
                             @ModelAttribute Details details,
                             @RequestParam("banner") MultipartFile banner) {
        tempUrl.put("bannerUrl", banner);
        newBuildingService.saveMainDetails(newBuilding, details);
        return "redirect:/admin/main/new-buildings/create-description";
    }

    @GetMapping("/create-description")
    public String showCreateDescription(@ModelAttribute("newBuilding") NewBuilding newBuilding, Model model, HttpServletRequest request) {
        model.addAttribute("information", newBuilding.getInformation() != null ? newBuilding.getInformation() : new Information());
        model.addAttribute("currentURI", request.getRequestURI());
        return "admin/main/new-building/create/create-description";
    }

    @PostMapping("/create-description")
    public String createDescription(@ModelAttribute("newBuilding") NewBuilding newBuilding,
                                    @ModelAttribute Information information,
                                    @RequestParam("photos") List<MultipartFile> photos) {
        tempUrls.put("photoUrls", photos);
        newBuildingService.saveDescriptionDetails(newBuilding, information);
        return "redirect:/admin/main/new-buildings/create-location";
    }

    @GetMapping("/create-location")
    public String showCreateLocation(@ModelAttribute("newBuilding") NewBuilding newBuilding, Model model, HttpServletRequest request) {
        model.addAttribute("address", newBuilding.getAddress() != null ? newBuilding.getAddress() : new Address());
        model.addAttribute("information", newBuilding.getInformation() != null ? newBuilding.getInformation() : new Information());

        model.addAttribute("currentURI", request.getRequestURI());
        return "admin/main/new-building/create/create-location";
    }

    @PostMapping("/create-location")
    public String createLocation(@ModelAttribute("newBuilding") NewBuilding newBuilding,
                                 @ModelAttribute Address address,
                                 @ModelAttribute Information information) {
        newBuildingService.saveLocationDetails(newBuilding, address, information);
        return "redirect:/admin/main/new-buildings/create-infrastructure";
    }

    @GetMapping("/create-infrastructure")
    public String showCreateInfrastructure(@ModelAttribute("newBuilding") NewBuilding newBuilding, Model model, HttpServletRequest request) {
        model.addAttribute("newBuildingDetails", newBuilding.getDetails() != null ? newBuilding.getDetails() : new Details());
        model.addAttribute("information", newBuilding.getInformation() != null ? newBuilding.getInformation() : new Information());

        model.addAttribute("currentURI", request.getRequestURI());
        return "admin/main/new-building/create/create-infrastructure";
    }

    @PostMapping("/create-infrastructure")
    public String createInfrastructure(@ModelAttribute("newBuilding") NewBuilding newBuilding,
                                       @ModelAttribute Information information,
                                       @ModelAttribute Details details,
                                       @RequestParam("photos") List<MultipartFile> photos) {
        newBuildingService.saveInfrastructureDetails(newBuilding, information, details);
        tempUrls.put("infrastructurePhotoUrls", photos);
        return "redirect:/admin/main/new-buildings/create-panorama";
    }

    @GetMapping("/create-panorama")
    public String showCreatePanorama(@ModelAttribute("newBuilding") NewBuilding newBuilding, Model model, HttpServletRequest request) {
        model.addAttribute("currentURI", request.getRequestURI());
        return "admin/main/new-building/create/create-panorama";
    }

    @PostMapping("/create-panorama")
    public String createPanorama(@ModelAttribute("newBuilding") NewBuilding newBuilding,
                                 @RequestParam("panorama") MultipartFile panorama) {
        tempUrl.put("panoramaUrl", panorama);
        return "redirect:/admin/main/new-buildings/create-specification";
    }

    @GetMapping("/create-specification")
    public String showCreateSpecification(@ModelAttribute("newBuilding") NewBuilding newBuilding, Model model, HttpServletRequest request) {
        model.addAttribute("information", newBuilding.getInformation());
        model.addAttribute("currentURI", request.getRequestURI());
        return "admin/main/new-building/create/create-specification";
    }

    @PostMapping("/create-specification")
    public String createSpecification(@ModelAttribute("newBuilding") NewBuilding newBuilding,
                                      @ModelAttribute Information information,
                                      SessionStatus status) {
        newBuildingService.create(newBuilding);
        newBuildingService.addAllPhotoUrls(newBuilding, tempUrl, tempUrls);

        tempUrl.clear();
        tempUrls.clear();

        status.setComplete();
        return "redirect:/admin/main/new-buildings";
    }
}
