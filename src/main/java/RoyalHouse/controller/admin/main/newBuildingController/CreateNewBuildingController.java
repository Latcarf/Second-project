package RoyalHouse.controller.admin.main.newBuildingController;

import RoyalHouse.model.building.*;
import RoyalHouse.service.PhotoService;
import RoyalHouse.service.admin.main.NewBuildingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;

@Controller
@RequestMapping("/admin/main/new-buildings")
@SessionAttributes("newBuilding")
public class CreateNewBuildingController {

    private final NewBuildingService newBuildingService;
    private final PhotoService photoService;

    public CreateNewBuildingController(NewBuildingService newBuildingService, PhotoService photoService) {
        this.newBuildingService = newBuildingService;
        this.photoService = photoService;
    }

    @ModelAttribute("newBuilding")
    public NewBuilding newBuilding() {
        return new NewBuilding();
    }

    @GetMapping("/create-main")
    public String showCreateMain(@ModelAttribute("newBuilding") NewBuilding newBuilding, Model model) {
        model.addAttribute("newBuildingDetails", new Details());
        return "admin/main/new-building/create/create-main";
    }

    @PostMapping("/create-main")
    public String createMain(@ModelAttribute("newBuilding") NewBuilding newBuilding,
                             @ModelAttribute Details details,
                             @RequestParam("banner") MultipartFile banner) {
        String bannerUrl = photoService.saveNewBuildingBanner(banner, newBuilding.getName(), newBuilding.getId());

        newBuildingService.saveMainDetails(newBuilding, details, bannerUrl);

        return "redirect:/admin/main/new-buildings/create-about";
    }

    @GetMapping("/create-about")
    public String showCreateAbout(@ModelAttribute("newBuilding") NewBuilding newBuilding, Model model) {
        model.addAttribute("information", new Information());
        return "admin/main/new-building/create/create-about";
    }

    @PostMapping("/create-about")
    public String createAbout(@ModelAttribute("newBuilding") NewBuilding newBuilding,
                              @ModelAttribute Information information,
                              @RequestParam("photos") List<MultipartFile> photos) {
        // Логика сохранения данных второго шага в объекте newBuilding
        newBuilding.setInformation(information);
        // Загрузка фото и сохранение URL
        // Допустим, что `newBuilding.getPhotoUrls().add()` добавляет URL фото
        // for (MultipartFile photo : photos) {
        //     newBuilding.getPhotoUrls().add(uploadPhoto(photo));
        // }

        return "redirect:/admin/main/new-buildings/create-location";
    }

    @GetMapping("/create-location")
    public String showCreateLocation(@ModelAttribute("newBuilding") NewBuilding newBuilding, Model model) {
        model.addAttribute("address", new Address());
        return "admin/main/new-building/create-location";
    }

    @PostMapping("/create-location")
    public String createLocation(@ModelAttribute("newBuilding") NewBuilding newBuilding,
                                 @ModelAttribute Address address) {
        // Логика сохранения данных третьего шага в объекте newBuilding
        newBuilding.setAddress(address);

        return "redirect:/admin/main/new-buildings/create-infrastructure";
    }

    @GetMapping("/create-infrastructure")
    public String showCreateInfrastructure(@ModelAttribute("newBuilding") NewBuilding newBuilding, Model model) {
        model.addAttribute("information", newBuilding.getInformation());
        return "admin/main/new-building/create-infrastructure";
    }

    @PostMapping("/create-infrastructure")
    public String createInfrastructure(@ModelAttribute("newBuilding") NewBuilding newBuilding,
                                       @ModelAttribute Information information,
                                       @RequestParam("photos") List<MultipartFile> photos) {
        // Логика сохранения данных четвертого шага в объекте newBuilding
        newBuilding.getInformation().setInfrastructure(information.getInfrastructure());
        // Загрузка фото и сохранение URL
        // for (MultipartFile photo : photos) {
        //     newBuilding.getInformation().getInfrastructurePhotoUrls().add(uploadPhoto(photo));
        // }

        return "redirect:/admin/main/new-buildings/create-panorama";
    }

    @GetMapping("/create-panorama")
    public String showCreatePanorama(@ModelAttribute("newBuilding") NewBuilding newBuilding, Model model) {
        return "admin/main/new-building/create-panorama";
    }

    @PostMapping("/create-panorama")
    public String createPanorama(@ModelAttribute("newBuilding") NewBuilding newBuilding,
                                 @RequestParam("photos") List<MultipartFile> photos) {
        // Логика сохранения данных пятого шага в объекте newBuilding
        // Загрузка панорамного фото и сохранение URL
        // newBuilding.getPhotoUrls().add(uploadPhoto(photos.get(0)));

        return "redirect:/admin/main/new-buildings/create-specification";
    }

    @GetMapping("/create-specification")
    public String showCreateSpecification(@ModelAttribute("newBuilding") NewBuilding newBuilding, Model model) {
        model.addAttribute("information", newBuilding.getInformation());
        return "admin/main/new-building/create-specification";
    }

    @PostMapping("/create-specification")
    public String createSpecification(@ModelAttribute("newBuilding") NewBuilding newBuilding,
                                      @ModelAttribute Information information,
                                      SessionStatus status) {
        // Логика сохранения данных шестого шага в объекте newBuilding
        newBuilding.getInformation().setSpecification(information.getSpecification());

        // Сохранение всех данных в базе данных
        newBuildingService.create(newBuilding);

        // Завершение сессии
        status.setComplete();

        return "redirect:/admin/main/new-buildings";
    }
}
