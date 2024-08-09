package RoyalHouse.repository;

import RoyalHouse.service.admin.main.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealEstateRepository extends JpaRepository<RealEstate, Long> {
}
