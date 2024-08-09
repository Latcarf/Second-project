package RoyalHouse.service.admin.main;

import RoyalHouse.repository.RealEstateRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RealEstate {
    private final RealEstateRepository realEstateRepository;

    public RealEstate(RealEstateRepository realEstateRepository) {
        this.realEstateRepository = realEstateRepository;
    }

    public RealEstate getRealEstateById(Long realEstateId) {
        return realEstateRepository.findById(realEstateId)
                .orElseThrow(() -> new IllegalArgumentException("RealEstateId: " + realEstateId + " Not Found"));
    }

//    public List<RealEstate> getAllRealEstates(Long id, String type, int room, PageRequest pageRequest) {
//        return realEstateRepository.findAll(spec, pageRequest);
//
//    }

}
