package RoyalHouse.service.admin.main;

import RoyalHouse.model.building.NewBuilding;
import RoyalHouse.model.building.RealEstate;
import RoyalHouse.repository.building.NewBuildingRepository;
import RoyalHouse.specification.NewBuildingSpecification;
import RoyalHouse.specification.RealEstateSpecification;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class NewBuildingService {
    private final NewBuildingRepository newBuildingRepository;

    public NewBuildingService(NewBuildingRepository newBuildingRepository) {
        this.newBuildingRepository = newBuildingRepository;
    }

    public Page<NewBuilding> getNewBuildings(String name, String address, String status, PageRequest pageRequest) {
        Specification<NewBuilding> spec = Specification.where(null);

        if (StringUtils.isNotBlank(name)) {
            spec = spec.and(NewBuildingSpecification.hasName(name));
        }
        if (StringUtils.isNotBlank(address)) {
            spec = spec.and(NewBuildingSpecification.hasAddress(address));
        }
        if (Objects.nonNull(status)) {
            spec = spec.and(NewBuildingSpecification.hasStatus(status));
        }

        return newBuildingRepository.findAll(spec, pageRequest);
    }

    public List<NewBuilding> getNewBuildings() {
        return newBuildingRepository.findAll();
    }

    public NewBuilding getNewBuildingById(Long id) {
        return newBuildingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No New Building found with id: " + id));
    }

    public List<NewBuilding> searchNewBuildings(String query) {
        return newBuildingRepository.findByNameContainingIgnoreCase(query);
    }
}