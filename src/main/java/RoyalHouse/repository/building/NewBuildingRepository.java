package RoyalHouse.repository.building;

import RoyalHouse.model.building.NewBuilding;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface NewBuildingRepository extends JpaRepository<NewBuilding, Long>, JpaSpecificationExecutor<NewBuilding> {
    List<NewBuilding> findByNameContainingIgnoreCase(String name);
}
