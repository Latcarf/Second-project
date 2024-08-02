package RoyalHouse.repository;

import RoyalHouse.model.RequestEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestEmailRepository extends JpaRepository<RequestEmail, Long> {
    List<RequestEmail> findByContactId(Long contactId);
    RequestEmail findByEmailAndContactId(String email, Long contactId);
}
