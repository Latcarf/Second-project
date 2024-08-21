package RoyalHouse.repository.building;

import RoyalHouse.model.building.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RealEstateRepository extends JpaRepository<RealEstate, Long>, JpaSpecificationExecutor<RealEstate> {
}
