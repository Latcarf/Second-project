package RoyalHouse.service.admin.main;

import RoyalHouse.model.Request;
import RoyalHouse.repository.RequestRepository;
import RoyalHouse.model.modelEnum.Status;
import RoyalHouse.specification.RequestSpecification;
import RoyalHouse.util.Exception.*;
import RoyalHouse.util.RegEx;
import io.micrometer.common.util.StringUtils;
import org.slf4j.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class RequestService {
    private static final Logger logger = LoggerFactory.getLogger(RequestService.class);

    private final RequestRepository requestRepository;

    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public void createRequest(Request request) {
        logger.info("Creating a request for a user with email: {}", request.getEmail());

        validateRequest(request);

        Request newRequest = Request.builder()
                .userName(request.getUserName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .description(request.getDescription())
                .date(LocalDateTime.now())
                .status(Status.NEW.name())
                .build();

        Request savedRequest = requestRepository.save(newRequest);

        logger.info("Request successfully created with ID: {}", savedRequest.getId());
    }

    public Request getRequestById(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request with ID: " + requestId + " not found"));
    }

    public Page<Request> getRequests(String name, String phone, String email, LocalDate date, String status, PageRequest pageRequest) {
        Specification<Request> spec = Specification.where(null);

        if (StringUtils.isNotBlank(name)) {
            spec = spec.and(RequestSpecification.hasName(name));
        }
        if (StringUtils.isNotBlank(phone)) {
            spec = spec.and(RequestSpecification.hasPhone(phone));
        }
        if (StringUtils.isNotBlank(email)) {
            spec = spec.and(RequestSpecification.hasEmail(email));
        }
        if (Objects.nonNull(date)) {
            spec = spec.and(RequestSpecification.hasDate(date));
        }
        if (StringUtils.isNotBlank(status)) {
            spec = spec.and(RequestSpecification.hasStatus(status));
        }

        return requestRepository.findAll(spec, pageRequest);
    }

    public void changeStatus(Long requestId) {
        Request request = getRequestById(requestId);
        if (request.getStatus().equals(Status.NEW.toString())) {
            request.setStatus(String.valueOf(Status.ANSWERED));
        } else {
            request.setStatus(String.valueOf(Status.NEW));
        }
        requestRepository.save(request);
    }

    public void deleteRequest(Long requestId) {
        requestRepository.deleteById(requestId);
    }

    private void validateRequest(Request request) {
        if (Objects.isNull(request.getDescription()) || request.getDescription().trim().isEmpty()) {
            throw new MissingDescriptionException("Description is required.");
        }

        if (request.getDescription().length() < 10 || request.getDescription().length() > 500) {
            throw new MissingDescriptionLengthException("Description must be between 10 and 500 characters.");
        }

        if (!RegEx.USERNAME_REGEX.matcher(request.getUserName()).matches()) {
            throw new MissingUserNameException("User name format is invalid.");
        }

        if (Objects.isNull(request.getEmail()) || !RegEx.EMAIL_REGEX.matcher(request.getEmail()).matches()) {
            throw new InvalidEmailException("Invalid email format.");
        }

        if (Objects.isNull(request.getPhone()) || !RegEx.PHONE_REGEX.matcher(request.getPhone()).matches()) {
            throw new InvalidPhoneNumberException("Invalid phone number format.");
        }
    }
}

