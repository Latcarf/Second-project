package RoyalHouse.service.admin.main;

import RoyalHouse.model.modelEnum.Status;
import RoyalHouse.repository.ServiceRepository;
import RoyalHouse.service.PaginationService;
import RoyalHouse.service.PhotoService;
import RoyalHouse.specification.ServiceSpecification;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ServiceService implements PaginationService<RoyalHouse.model.company.Service> {
    private final ServiceRepository serviceRepository;
    private final PhotoService photoService;

    public ServiceService(ServiceRepository serviceRepository, PhotoService photoService) {
        this.serviceRepository = serviceRepository;
        this.photoService = photoService;
    }

    @Override
    public Page<RoyalHouse.model.company.Service> getPage(PageRequest pageRequest, Map<String, Object> filterParams) {
        Specification<RoyalHouse.model.company.Service> spec = Specification.where(null);

        String name = (String) filterParams.get("name");
        String status = (String) filterParams.get("status");

        if (StringUtils.isNotBlank(name)) {
            spec = spec.and(ServiceSpecification.hasName(name));
        }
        if (Objects.nonNull(status)) {
            spec = spec.and(ServiceSpecification.hasStatus(status));
        }

        return serviceRepository.findAll(spec, pageRequest);
    }

    public List<RoyalHouse.model.company.Service> getService() {
        return serviceRepository.findAll();
    }

    public RoyalHouse.model.company.Service getServiceById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Service found with id: " + id));
    }

    public void changeStatus(Long serviceId) {
        RoyalHouse.model.company.Service service = getServiceById(serviceId);
        if (service.getStatus().equals(Status.ACTIVE.toString())) {
            service.setStatus(String.valueOf(Status.DEACTIVATED));
        } else {
            service.setStatus(String.valueOf(Status.ACTIVE));
        }
        serviceRepository.save(service);
    }

    @Transactional
    public void createService(RoyalHouse.model.company.Service service) {
        if (Status.ACTIVE.toString().equals(service.getStatus())) {
            service.setStatus(Status.ACTIVE.toString());
        } else {
            service.setStatus(Status.DEACTIVATED.toString());
        }

        serviceRepository.save(service);
    }

    public void editService(RoyalHouse.model.company.Service service, MultipartFile preview, MultipartFile banner) {
        RoyalHouse.model.company.Service existingService = getServiceById(service.getId());

        existingService.setName(service.getName());
        existingService.setDescription(service.getDescription());

        if (Status.ACTIVE.toString().equals(service.getStatus())) {
            existingService.setStatus(Status.ACTIVE.toString());
        } else {
            existingService.setStatus(Status.DEACTIVATED.toString());
        }

        if (!preview.isEmpty()) {
            photoService.deleteSpecificPhoto(existingService.getPhotoUrl());
            String photoUrl = photoService.saveServicePhoto(preview, existingService.getName(), existingService.getId());
            existingService.setPhotoUrl(photoUrl);
        }

        if (!banner.isEmpty()) {
            photoService.deleteSpecificPhoto(existingService.getBannerUrl());
            String bannerUrl = photoService.saveServiceBanner(banner, existingService.getName(), existingService.getId());
            existingService.setBannerUrl(bannerUrl);
        }

        serviceRepository.save(existingService);
    }

    public void deleteService(Long id) {
        RoyalHouse.model.company.Service service = getServiceById(id);

        photoService.deleteSpecificPhoto(service.getPhotoUrl());

        serviceRepository.delete(service);
    }
}
