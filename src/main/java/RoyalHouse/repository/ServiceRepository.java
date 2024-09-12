package RoyalHouse.repository;

import RoyalHouse.model.company.Service;
import RoyalHouse.model.modelEnum.Status;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Long>, JpaSpecificationExecutor<Service> {
    List<Service> findByStatus(String status);
}
