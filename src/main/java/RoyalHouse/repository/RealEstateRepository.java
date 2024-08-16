package RoyalHouse.repository;

import RoyalHouse.model.Building.RealEstate;
import RoyalHouse.service.admin.main.RealEstateService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RealEstateRepository extends JpaRepository<RealEstate, Long>, JpaSpecificationExecutor<RealEstate> {
}
