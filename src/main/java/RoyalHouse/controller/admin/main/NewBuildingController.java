package RoyalHouse.controller.admin.main;

import RoyalHouse.model.building.Address;
import RoyalHouse.model.building.NewBuilding;
import RoyalHouse.service.admin.main.NewBuildingService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/main/new-buildings")
public class NewBuildingController {

    private final NewBuildingService newBuildingService;

    public NewBuildingController(NewBuildingService newBuildingService) {
        this.newBuildingService = newBuildingService;
    }

    @GetMapping
    public List<NewBuilding> getNewBuildings(@RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "5") int size,
                                             @RequestParam(value = "name", required = false) String name,
                                             @RequestParam(value = "address", required = false) String address,
                                             @RequestParam(value = "status", required = false) String status,
                                             Model model) {
        return null;
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
