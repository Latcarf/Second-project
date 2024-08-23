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

    public NewBuilding getNewBuildingById(Long id) {
        return newBuildingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No New Building found with id: " + id));
    }

    public List<NewBuilding> searchNewBuildings(String query) {
        return newBuildingRepository.findByNameContainingIgnoreCase(query);
    }
}