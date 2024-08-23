package RoyalHouse.repository.building;

import RoyalHouse.model.building.NewBuilding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewBuildingRepository extends JpaRepository<NewBuilding, Long> {
    List<NewBuilding> findByNameContainingIgnoreCase(String name);
}
