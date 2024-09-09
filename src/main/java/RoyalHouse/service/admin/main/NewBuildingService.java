package RoyalHouse.service.admin.main;

import RoyalHouse.model.building.Address;
import RoyalHouse.model.building.Details;
import RoyalHouse.model.building.Information;
import RoyalHouse.model.building.NewBuilding;
import RoyalHouse.model.modelEnum.Status;
import RoyalHouse.repository.building.AddressRepository;
import RoyalHouse.repository.building.DetailsRepository;
import RoyalHouse.repository.building.InformationRepository;
import RoyalHouse.repository.building.NewBuildingRepository;
import RoyalHouse.service.PaginationService;
import RoyalHouse.service.PhotoService;
import RoyalHouse.specification.NewBuildingSpecification;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class NewBuildingService implements PaginationService<NewBuilding> {
    private final NewBuildingRepository newBuildingRepository;
    private final InformationRepository informationRepository;

    private final PhotoService photoService;


    public NewBuildingService(NewBuildingRepository newBuildingRepository, InformationRepository informationRepository, PhotoService photoService) {
        this.newBuildingRepository = newBuildingRepository;
        this.informationRepository = informationRepository;
        this.photoService = photoService;
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

    public void deleteNewBuilding(Long id) {
        NewBuilding newBuilding = getNewBuildingById(id);

        photoService.deleteSpecificPhotos(newBuilding.getPhotoUrls());
        photoService.deleteSpecificPhoto(newBuilding.getBannerUrl());
        photoService.deleteSpecificPhoto(newBuilding.getPanoramaUrl());
        photoService.deleteSpecificPhotos(newBuilding.getInformation().getInfrastructurePhotoUrls());

        newBuildingRepository.delete(newBuilding);
    }

    public void saveMainDetails(NewBuilding newBuilding, Details details) {
        if ("ACTIVE".equals(newBuilding.getStatus())) {
            newBuilding.setStatus(Status.ACTIVE.toString());
        } else {
            newBuilding.setStatus(Status.DEACTIVATED.toString());
        }

        setSortingOrder(newBuilding);
        newBuilding.setName(newBuilding.getName());
        newBuilding.setNumApartment(newBuilding.getNumApartment());

        newBuilding.getDetails().setYearBuilt(details.getYearBuilt());
        newBuilding.getDetails().setNumFloors(details.getNumFloors());
    }

    public void saveDescriptionDetails(NewBuilding newBuilding, Information information) {
        newBuilding.getInformation().setDescription(information.getDescription());
    }

    public void saveLocationDetails(NewBuilding newBuilding, Address address, Information information) {
        newBuilding.setAddress(address);
        newBuilding.getInformation().setLocation(information.getLocation());
    }

    public void saveInfrastructureDetails(NewBuilding newBuilding, Information information, Details details) {
        newBuilding.getInformation().setInfrastructure(information.getInfrastructure());

        newBuilding.getDetails().setNumCommercialPremises(details.getNumCommercialPremises());
        newBuilding.getDetails().setNumRecreationAreas(details.getNumRecreationAreas());
        newBuilding.getDetails().setNumParkingSpaces(details.getNumParkingSpaces());

        newBuilding.getDetails().setPlayground(Objects.nonNull(details.getPlayground()) ? details.getPlayground() : false);
        newBuilding.getDetails().setFitness(Objects.nonNull(details.getFitness()) ? details.getFitness() : false);
        newBuilding.getDetails().setSwimmingPool(Objects.nonNull(details.getSwimmingPool()) ? details.getSwimmingPool() : false);
    }


    public void saveSpecificationDetails(NewBuilding newBuilding, List<String> specifications) {
        newBuilding.getInformation().setSpecifications(specifications);
    }

    public void create(NewBuilding newBuilding) {
        newBuildingRepository.save(newBuilding);
    }

    public void addAllPhotoUrls(NewBuilding newBuilding, Map<String, MultipartFile> url, Map<String, List<MultipartFile>> urls) {
        if (Objects.nonNull(url.get("bannerUrl"))) {
            String bannerUrl = photoService.saveNewBuildingBanner(url.get("bannerUrl"), newBuilding.getName(), newBuilding.getId());
            newBuilding.setBannerUrl(bannerUrl);
        }

        if (Objects.nonNull(urls.get("photoUrls"))) {
            List<String> photosUrls = photoService.saveNewBuildingPhotos(urls.get("photoUrls"), newBuilding.getName(), newBuilding.getId());
            newBuilding.setPhotoUrls(photosUrls);
        }

        if (Objects.nonNull(urls.get("infrastructurePhotoUrls"))) {
            List<String> photosUrls = photoService.saveInfrastructurePhotos(urls.get("infrastructurePhotoUrls"), newBuilding.getName(), newBuilding.getId());
            newBuilding.getInformation().setInfrastructurePhotoUrls(photosUrls);
        }

        if (Objects.nonNull(url.get("panoramaUrl"))) {
            String panoramaUrl = photoService.saveNewBuildingPanorama(url.get("panoramaUrl"), newBuilding.getName(), newBuilding.getId());
            newBuilding.setPanoramaUrl(panoramaUrl);
        }
        newBuildingRepository.save(newBuilding);
    }

    private void setSortingOrder(NewBuilding newBuilding) {
        List<NewBuilding> allBuildings = getNewBuildings();
        Integer sortingOrder = newBuilding.getSortingOrder();

        int maxSortingOrder = allBuildings.stream()
                .max(Comparator.comparingInt(NewBuilding::getSortingOrder))
                .map(NewBuilding::getSortingOrder)
                .orElse(0);

        if (sortingOrder > maxSortingOrder || sortingOrder <= 0) {
            newBuilding.setSortingOrder(maxSortingOrder + 1);
        } else {
            boolean orderExists = allBuildings.stream()
                    .anyMatch(building -> building.getSortingOrder().equals(sortingOrder));

            if (orderExists) {
                newBuilding.setSortingOrder(maxSortingOrder + 1);
            } else {
                newBuilding.setSortingOrder(sortingOrder);
            }
        }
    }
}