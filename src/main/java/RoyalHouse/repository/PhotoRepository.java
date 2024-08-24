package RoyalHouse.repository;

import RoyalHouse.model.Photo;
import RoyalHouse.model.modelEnum.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByEntityIdAndEntityType(Long realEstateId, EntityType entityType);
}
