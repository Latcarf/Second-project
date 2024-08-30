package RoyalHouse.repository.building;

import RoyalHouse.model.building.Information;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InformationRepository extends JpaRepository<Information, Long> {
}
