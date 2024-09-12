package RoyalHouse.controller.user;

import RoyalHouse.dto.Pagination;
import RoyalHouse.model.building.NewBuilding;
import RoyalHouse.model.building.RealEstate;

import RoyalHouse.model.building.RealEstateType;
import RoyalHouse.service.admin.main.NewBuildingService;
import RoyalHouse.service.admin.main.RealEstateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/main")
public class MainController {

    private final NewBuildingService newBuildingService;
    private final RealEstateService realEstateService;

    public MainController(NewBuildingService newBuildingService, RealEstateService realEstateService) {
        this.newBuildingService = newBuildingService;
        this.realEstateService = realEstateService;
    }

    @GetMapping
    public String mainPage(Model model) {
        List<NewBuilding> newBuildings = newBuildingService.getAllSortedNewBuildings().stream()
                .limit(8)
                .collect(Collectors.toList());

        List<RealEstate> randomApartments = realEstateService.getRandomApartments();

        model.addAttribute("newBuildings", newBuildings);
        model.addAttribute("randomApartments", randomApartments);

        return "user/main";
    }

    @GetMapping("/new-building-details/{id}")
    public String newBuildingDetails(@PathVariable Long id, Model model) {
        NewBuilding newBuilding = newBuildingService.getNewBuildingById(id);
        List<RealEstate> apartments = realEstateService.getApartments(id);
        int[] apartmentsSqMArray = realEstateService.getApartmentsSqM(newBuilding.getId());
        List<NewBuilding> newBuildings = newBuildingService.getAllSortedNewBuildings().stream()
                .limit(6)
                .collect(Collectors.toList());

        model.addAttribute("newBuilding", newBuilding);
        model.addAttribute("apartments", apartments);
        model.addAttribute("apartmentsSqMArray", apartmentsSqMArray);
        model.addAttribute("newBuildings", newBuildings);
        return "/user/new-building-details";
    }

    @GetMapping("/real-estates")
    public String getRealEstatesWithFilter(@RequestParam(value = "type", required = false) String type,
                                           @RequestParam(value = "city", required = false) List<String> cities,
                                           @RequestParam(value = "district", required = false) List<String> districts,
                                           @RequestParam(value = "housingStatus", required = false) List<String> housingStatuses,
                                           Model model) {

        Map<String, Object> filterParams = new HashMap<>();
        if (Objects.nonNull(type)) filterParams.put("type", type);
        if (Objects.nonNull(cities)) filterParams.put("city", cities);
        if (Objects.nonNull(districts)) filterParams.put("district", districts);
        if (Objects.nonNull(housingStatuses)) filterParams.put("housingStatus", housingStatuses);

        Map<String, Integer> realEstateCounts = new HashMap<>();
        realEstateCounts.put(RealEstateType.APARTMENT.toString(), realEstateService.getQuantityByType(RealEstateType.APARTMENT.toString()));
        realEstateCounts.put(RealEstateType.HOUSE.toString(), realEstateService.getQuantityByType(RealEstateType.HOUSE.toString()));
        realEstateCounts.put(RealEstateType.COMMERCIAL.toString(), realEstateService.getQuantityByType(RealEstateType.COMMERCIAL.toString()));
        realEstateCounts.put(RealEstateType.PLOT.toString(), realEstateService.getQuantityByType(RealEstateType.PLOT.toString()));

        List<RealEstate> filteredRealEstates = realEstateService.getFilteredRealEstate(filterParams);

        model.addAttribute("realEstates", filteredRealEstates);
        model.addAttribute("filterParams", filterParams);
        model.addAttribute("realEstateCounts", realEstateCounts);

        return "user/real-estate";
    }



    @GetMapping("/real-estate-details/{id}")
    public String realEstateDetails(@PathVariable Long id, Model model) {
        RealEstate realEstate = realEstateService.getRealEstateById(id);
        model.addAttribute("realEstate", realEstate);
        return "user/real-estate-details";
    }
}
