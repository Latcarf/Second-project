package RoyalHouse.service.admin.main;

import RoyalHouse.model.building.Address;
import RoyalHouse.model.building.Details;
import RoyalHouse.model.building.NewBuilding;
import RoyalHouse.model.building.RealEstate;
import RoyalHouse.repository.building.DetailsRepository;
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

import java.util.*;
import java.util.stream.Collectors;

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

    public List<RealEstate> getRandomApartments() {
        List<RealEstate> allApartments = realEstateRepository.findAll();

        if (allApartments.isEmpty()) {
            return List.of();
        }

        List<RealEstate> filteredApartments = allApartments.stream()
                .filter(realEstate -> realEstate.getType().equals("APARTMENT"))
                .collect(Collectors.toList());

        Collections.shuffle(filteredApartments);

        return filteredApartments.stream()
                .limit(21)
                .collect(Collectors.toList());
    }

    public List<RealEstate> getApartments(Long newBuildingId) {
        List<RealEstate> apartments = realEstateRepository.findByNewBuildingId(newBuildingId);

        if (apartments.isEmpty()) {
            return List.of();
        }

        return apartments;
    }

    public int[] getApartmentsSqM(Long newBuildingId) {
        List<RealEstate> apartments = getApartments(newBuildingId);

        int minSqM = apartments.stream()
                .mapToInt(RealEstate::getAreaSqM)
                .min()
                .orElse(0);

        int maxSqM = apartments.stream()
                .mapToInt(RealEstate::getAreaSqM)
                .max()
                .orElse(0);

        return new int[]{minSqM, maxSqM};
    }

    public List<RealEstate> getFilteredRealEstate(Map<String, Object> filterParams) {
        Specification<RealEstate> spec = Specification.where(null);

        String type = (String) filterParams.get("type");
        if (StringUtils.isNotBlank(type)) {
            spec = spec.and(RealEstateSpecification.hasType(type));
        }

        List<String> cities = (List<String>) filterParams.get("city");
        if (cities != null && !cities.isEmpty()) {
            spec = spec.and(RealEstateSpecification.hasCities(cities));
        }

        List<String> districts = (List<String>) filterParams.get("district");
        if (districts != null && !districts.isEmpty()) {
            spec = spec.and(RealEstateSpecification.hasDistricts(districts));
        }

        List<String> housingStatuses = (List<String>) filterParams.get("housingStatus");
        if (housingStatuses != null && !housingStatuses.isEmpty()) {
            for (String status : housingStatuses) {
                if ("before2000".equals(status)) {
                    spec = spec.and(RealEstateSpecification.builtBeforeOrAfter(2000, true));
                } else if ("after2020".equals(status)) {
                    spec = spec.and(RealEstateSpecification.builtBeforeOrAfter(2020, false));
                } else if ("underConstruction".equals(status)) {
                    spec = spec.and(RealEstateSpecification.isUnderConstruction());
                }
            }
        }

        List<RealEstate> filteredRealEstates = realEstateRepository.findAll(spec);
        Collections.shuffle(filteredRealEstates);
        return filteredRealEstates;
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

        photoService.deleteSpecificPhotos(realEstate.getPhotoUrls());

        realEstateRepository.delete(realEstate);
    }

    public Integer getQuantityByType(String type) {
        List<RealEstate> realEstates = realEstateRepository.findByType(type);
        return realEstates.size();
    }
}
