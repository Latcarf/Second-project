package RoyalHouse.service.admin.main;

import RoyalHouse.model.Request;
import RoyalHouse.repository.RequestRepository;
import RoyalHouse.model.Enum.Status;
import RoyalHouse.util.Exception.*;
import RoyalHouse.util.RegEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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

    public List<Request> getAllRequest() {
        return requestRepository.findAll();
    }

    public void deleteRequest(Long requestId) {
        requestRepository.deleteById(requestId);
    }

    public Page<Request> findPaginated(PageRequest of) {
        return null;
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

