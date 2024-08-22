package RoyalHouse.service.admin.main;

import RoyalHouse.model.building.NewBuilding;
import RoyalHouse.repository.building.NewBuildingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewBuildingService {
    private final NewBuildingRepository newBuildingRepository;

    public NewBuildingService(NewBuildingRepository newBuildingRepository) {
        this.newBuildingRepository = newBuildingRepository;
    }

    public List<NewBuilding> getNewBuildings() {
        return newBuildingRepository.findAll();
    }
}