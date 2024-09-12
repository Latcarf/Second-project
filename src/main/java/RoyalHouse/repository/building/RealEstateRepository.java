package RoyalHouse.repository.building;

import RoyalHouse.model.building.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RealEstateRepository extends JpaRepository<RealEstate, Long>, JpaSpecificationExecutor<RealEstate> {
    List<RealEstate> findByNewBuildingId(Long newBuildingId);

    List<RealEstate> findByType(String type);
}
