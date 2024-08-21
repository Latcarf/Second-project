package RoyalHouse.service.admin.main;

import RoyalHouse.model.Photo;
import RoyalHouse.model.building.Address;
import RoyalHouse.model.building.NewBuilding;
import RoyalHouse.model.building.RealEstate;
import RoyalHouse.repository.PhotoRepository;
import RoyalHouse.repository.building.AddressRepository;
import RoyalHouse.repository.building.NewBuildingRepository;
import RoyalHouse.repository.building.RealEstateRepository;
import RoyalHouse.specification.RealEstateSpecification;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RealEstateService {
    private final RealEstateRepository realEstateRepository;
    private final AddressRepository addressRepository;
    private final NewBuildingRepository newBuildingRepository;
    private final PhotoRepository photoRepository;

    public RealEstateService(RealEstateRepository realEstateRepository, AddressRepository addressRepository,
                             NewBuildingRepository newBuildingRepository, PhotoRepository photoRepository) {
        this.realEstateRepository = realEstateRepository;
        this.addressRepository = addressRepository;
        this.newBuildingRepository = newBuildingRepository;
        this.photoRepository = photoRepository;
    }

    public RealEstate getRealEstateById(Long realEstateId) {
        return realEstateRepository.findById(realEstateId)
                .orElseThrow(() -> new IllegalArgumentException("RealEstateId: " + realEstateId + " Not Found"));
    }

    @Transactional
    public RealEstate createRealEstate(RealEstate realEstate, Address address, Long newBuildingId, List<Photo> photos) {
        address = addressRepository.save(address);
        realEstate.setAddress(address);

        if (newBuildingId != null) {
            NewBuilding newBuilding = newBuildingRepository.findById(newBuildingId)
                    .orElseThrow(() -> new IllegalArgumentException("New Building with ID " + newBuildingId + " not found"));
            realEstate.setNewBuilding(newBuilding);
        }

        realEstate = realEstateRepository.save(realEstate);

        for (Photo photo : photos) {
            photo.setEntityId(realEstate.getId());
            photoRepository.save(photo);
        }

        return realEstate;
    }

    public List<NewBuilding> getAllNewBuildings() {
        return newBuildingRepository.findAll();
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
