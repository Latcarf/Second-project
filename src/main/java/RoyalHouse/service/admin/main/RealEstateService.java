package RoyalHouse.service.admin.main;

import RoyalHouse.model.Photo;
import RoyalHouse.model.building.Address;
import RoyalHouse.model.building.Details;
import RoyalHouse.model.building.NewBuilding;
import RoyalHouse.model.building.RealEstate;
import RoyalHouse.model.modelEnum.EntityType;
import RoyalHouse.repository.building.AddressRepository;
import RoyalHouse.repository.DetailsRepository;
import RoyalHouse.repository.PhotoRepository;
import RoyalHouse.repository.building.NewBuildingRepository;
import RoyalHouse.repository.building.RealEstateRepository;
import RoyalHouse.specification.RealEstateSpecification;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
public class RealEstateService {
    private final RealEstateRepository realEstateRepository;
    private final AddressRepository addressRepository;
    private final DetailsRepository detailsRepository;
    private final NewBuildingRepository newBuildingRepository;
    private final PhotoRepository photoRepository;

    public RealEstateService(RealEstateRepository realEstateRepository, AddressRepository addressRepository,
                             DetailsRepository detailsRepository, NewBuildingRepository newBuildingRepository,
                             PhotoRepository photoRepository) {
        this.realEstateRepository = realEstateRepository;
        this.addressRepository = addressRepository;
        this.detailsRepository = detailsRepository;
        this.newBuildingRepository = newBuildingRepository;
        this.photoRepository = photoRepository;
    }

    public List<Photo> getPhotosForRealEstate(Long realEstateId) {
        return photoRepository.findByEntityIdAndEntityType(realEstateId, EntityType.REAL_ESTATE);
    }

    public RealEstate getRealEstateById(Long realEstateId) {
        return realEstateRepository.findById(realEstateId)
                .orElseThrow(() -> new IllegalArgumentException("RealEstateId: " + realEstateId + " Not Found"));
    }

    public void createRealEstate(RealEstate realEstate, Address address, Details details, Long newBuildingId) {
        address = addressRepository.save(address);
        realEstate.setAddress(address);

        details = detailsRepository.save(details);
        realEstate.setDetails(details);

        if (newBuildingId != null) {
            NewBuilding newBuilding = newBuildingRepository.findById(newBuildingId)
                    .orElseThrow(() -> new IllegalArgumentException("New Building with ID " + newBuildingId + " not found"));
            realEstate.setNewBuilding(newBuilding);
        }

        realEstateRepository.save(realEstate);
    }

    public void addPhotosToRealEstate(RealEstate realEstate, List<Photo> photos) {
        for (Photo photo : photos) {
            photo.setEntityId(realEstate.getId());
            photoRepository.save(photo);
        }
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
