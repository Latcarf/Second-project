package RoyalHouse.repository.building;

import RoyalHouse.model.building.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
