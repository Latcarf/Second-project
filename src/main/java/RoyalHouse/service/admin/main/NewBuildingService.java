package RoyalHouse.service.admin.main;

import RoyalHouse.model.building.Address;
import RoyalHouse.model.building.Details;
import RoyalHouse.model.building.Information;
import RoyalHouse.model.building.NewBuilding;
import RoyalHouse.model.modelEnum.Status;
import RoyalHouse.repository.building.DetailsRepository;
import RoyalHouse.repository.building.AddressRepository;
import RoyalHouse.repository.building.InformationRepository;
import RoyalHouse.repository.building.NewBuildingRepository;
import RoyalHouse.service.PaginationService;
import RoyalHouse.specification.NewBuildingSpecification;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class NewBuildingService implements PaginationService<NewBuilding> {
    private final NewBuildingRepository newBuildingRepository;
    private final DetailsRepository detailsRepository;
    private final AddressRepository addressRepository;
    private final InformationRepository informationRepository;

    public NewBuildingService(NewBuildingRepository newBuildingRepository, DetailsRepository detailsRepository, AddressRepository addressRepository, InformationRepository informationRepository) {
        this.newBuildingRepository = newBuildingRepository;
        this.detailsRepository = detailsRepository;
        this.addressRepository = addressRepository;
        this.informationRepository = informationRepository;
    }

    @Override
    public Page<NewBuilding> getPage(PageRequest pageRequest, Map<String, Object> filterParams) {
        Specification<NewBuilding> spec = Specification.where(null);

        String name = (String) filterParams.get("name");
        String address = (String) filterParams.get("address");
        String status = (String) filterParams.get("status");

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

    public void changeStatus(Long newBuildingId) {
        NewBuilding newBuilding = getNewBuildingById(newBuildingId);
        if (newBuilding.getStatus().equals(Status.ACTIVE.toString())) {
            newBuilding.setStatus(String.valueOf(Status.DEACTIVATED));
        } else {
            newBuilding.setStatus(String.valueOf(Status.ACTIVE));
        }
        newBuildingRepository.save(newBuilding);
    }

    public List<NewBuilding> searchNewBuildings(String query) {
        return newBuildingRepository.findByNameContainingIgnoreCase(query);
    }

    public void saveMainDetails(NewBuilding newBuilding, Details details, String bannerUrl) {
        newBuilding.setDetails(details);
        newBuilding.setBannerUrl(bannerUrl);
        newBuilding.setName(newBuilding.getName());
        newBuilding.setSortingOrder(newBuilding.getSortingOrder());
        newBuilding.setStatus(newBuilding.getStatus());
    }

    public void saveAboutDetails(NewBuilding newBuilding, Information information, List<String> photoUrls) {
        newBuilding.setInformation(information);
        newBuilding.setPhotoUrls(photoUrls);
    }

    public void saveLocationDetails(NewBuilding newBuilding, Address address) {
        newBuilding.setAddress(address);
    }

    public void saveInfrastructureDetails(NewBuilding newBuilding, String infrastructureText, List<String> infrastructurePhotoUrls) {
        newBuilding.getInformation().setInfrastructure(infrastructureText);
        newBuilding.getInformation().getInfrastructurePhotoUrls().addAll(infrastructurePhotoUrls);
    }

    public void savePanorama(NewBuilding newBuilding, String panoramaUrl) {
        newBuilding.setPanoramaUrl(panoramaUrl);
    }

    public void saveSpecificationDetails(NewBuilding newBuilding, String specificationText) {
        newBuilding.getInformation().setSpecification(specificationText);
    }

    public NewBuilding create(NewBuilding newBuilding) {
        detailsRepository.save(newBuilding.getDetails());
        addressRepository.save(newBuilding.getAddress());
        informationRepository.save(newBuilding.getInformation());

        return newBuildingRepository.save(newBuilding);
    }
}