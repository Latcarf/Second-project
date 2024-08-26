package RoyalHouse.service.admin.main;

import RoyalHouse.model.building.Address;
import RoyalHouse.model.building.Details;
import RoyalHouse.model.building.NewBuilding;
import RoyalHouse.model.building.RealEstate;
import RoyalHouse.repository.DetailsRepository;
import RoyalHouse.repository.building.AddressRepository;
import RoyalHouse.repository.building.NewBuildingRepository;
import RoyalHouse.repository.building.RealEstateRepository;
import RoyalHouse.service.PaginationService;
import RoyalHouse.service.PhotoService;
import RoyalHouse.specification.RealEstateSpecification;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

@Service
public class RealEstateService implements PaginationService<RealEstate> {
    private final RealEstateRepository realEstateRepository;
    private final AddressRepository addressRepository;
    private final DetailsRepository detailsRepository;
    private final NewBuildingRepository newBuildingRepository;
    private final PhotoService photoService;

    public RealEstateService(RealEstateRepository realEstateRepository, AddressRepository addressRepository,
                             DetailsRepository detailsRepository, NewBuildingRepository newBuildingRepository, PhotoService photoService) {
        this.realEstateRepository = realEstateRepository;
        this.addressRepository = addressRepository;
        this.detailsRepository = detailsRepository;
        this.newBuildingRepository = newBuildingRepository;
        this.photoService = photoService;
    }

    public RealEstate getRealEstateById(Long realEstateId) {
        return realEstateRepository.findById(realEstateId)
                .orElseThrow(() -> new IllegalArgumentException("RealEstateId: " + realEstateId + " Not Found"));
    }

    @Transactional
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

    @Override
    public Page<RealEstate> getPage(PageRequest pageRequest, Map<String, Object> filterParams) {
        Specification<RealEstate> spec = Specification.where(null);

        String name = (String) filterParams.get("name");
        String type = (String) filterParams.get("type");
        Integer room = (Integer) filterParams.get("room");

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
        RealEstate realEstate = getRealEstateById(id);

        photoService.deletePhotoFile(realEstate.getPhotoUrls());

        realEstateRepository.delete(realEstate);
    }
}
