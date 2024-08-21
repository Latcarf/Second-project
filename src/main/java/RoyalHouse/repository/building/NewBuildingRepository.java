package RoyalHouse.repository.building;

import RoyalHouse.model.building.NewBuilding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewBuildingRepository extends JpaRepository<NewBuilding, Long> {
}
