package RoyalHouse.controller.admin.main;

import RoyalHouse.dto.Pagination;
import RoyalHouse.model.building.Address;
import RoyalHouse.model.building.NewBuilding;
import RoyalHouse.service.admin.main.NewBuildingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin/main/new-buildings")
public class NewBuildingController {

    private final NewBuildingService newBuildingService;

    public NewBuildingController(NewBuildingService newBuildingService) {
        this.newBuildingService = newBuildingService;
    }

    @GetMapping
    public String getNewBuildings(@RequestParam(defaultValue = "1") int page,
                                             @RequestParam(value = "name", required = false) String name,
                                             @RequestParam(value = "address", required = false) String address,
                                             @RequestParam(value = "status", required = false) String status,
                                             Model model) {
        Map<String, Object> filterParams = new HashMap<>();
        if (Objects.nonNull(name)) filterParams.put("name", name);
        if (Objects.nonNull(address)) filterParams.put("address", address);
        if (Objects.nonNull(status)) filterParams.put("status", status);

        Pagination<NewBuilding> pagination = Pagination.create(page, newBuildingService, filterParams, "sortingOrder");

        model.addAttribute("newBuildings", pagination.getPageData());
        model.addAttribute("pagination", pagination);
        model.addAttribute("filterParams", filterParams);

        return "admin/main/new-building/new-buildings";
    }

        @GetMapping("/search")
        public List<NewBuilding> searchNewBuildings (@RequestParam("query") String query){
            return newBuildingService.searchNewBuildings(query);
        }

        @GetMapping("/get-num-floors")
        public Integer getNumFloors (@RequestParam("newBuildingId") Long newBuildingId){
            NewBuilding newBuilding = newBuildingService.getNewBuildingById(newBuildingId);
            return newBuilding.getDetails().getNumFloors();
        }

        @GetMapping("/get-address")
        public Address getAddress (@RequestParam("newBuildingId") Long newBuildingId){
            NewBuilding newBuilding = newBuildingService.getNewBuildingById(newBuildingId);
            return newBuilding.getAddress();
        }
    }
