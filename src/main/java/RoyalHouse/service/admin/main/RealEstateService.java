package RoyalHouse.service.admin.main;

import RoyalHouse.model.Building.RealEstate;
import RoyalHouse.repository.RealEstateRepository;
import RoyalHouse.specification.RealEstateSpecification;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RealEstateService {
    private final RealEstateRepository realEstateRepository;

    public RealEstateService(RealEstateRepository realEstateRepository) {
        this.realEstateRepository = realEstateRepository;
    }

    public RealEstate getRealEstateById(Long realEstateId) {
        return realEstateRepository.findById(realEstateId)
                .orElseThrow(() -> new IllegalArgumentException("RealEstateId: " + realEstateId + " Not Found"));
    }

    public Page<RealEstate> getRealEstates(String name, String type, Integer room, PageRequest pageRequest) {
        Specification<RealEstate> spec = Specification.where(null);

        if (StringUtils.isNotBlank(name)) {
            spec = spec.and(RealEstateSpecification.hasName(name));
        }
            if (StringUtils.isNotBlank(type)) {
            spec = spec.and(RealEstateSpecification.hasType(type));
        }
            if (Objects.nonNull(room)) {
            spec = spec.and(RealEstateSpecification.hasRoom(room));
        }

        return realEstateRepository.findAll(spec, pageRequest);
    }

    public void deleteRealEstate(Long id) {
        realEstateRepository.deleteById(id);
    }
}
