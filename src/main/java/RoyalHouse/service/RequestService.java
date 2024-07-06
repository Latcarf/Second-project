package RoyalHouse.service;

import RoyalHouse.dto.RequestDTO;
import RoyalHouse.convert.RequestConvert;
import RoyalHouse.model.Request;
import RoyalHouse.model.User.User;
import RoyalHouse.repository.RequestRepository;
import RoyalHouse.repository.UserRepository;
import RoyalHouse.model.Enum.Status;
import RoyalHouse.util.Exception.*;
import RoyalHouse.util.RegEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequestService {
    private static final Logger logger = LoggerFactory.getLogger(RequestService.class);

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestConvert requestConvert;

    public RequestService(RequestRepository requestRepository, UserRepository userRepository, RequestConvert requestConvert) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.requestConvert = requestConvert;
    }

    public void createRequest(RequestDTO requestDto) {
        logger.info("Creating a request for a user with email: {}", requestDto.getUserDTO().getEmail());

        validateRequest(requestDto);

        Request request = new Request();
        request.setDescription(requestDto.getDescription());
        request.setStatus(Status.NEW.name());
        request.setDate(LocalDateTime.now());
        request.setUser(getUser(requestDto));

        Request savedRequest = requestRepository.save(request);

        logger.info("Request successfully created with ID: {}", savedRequest.getId());
    }

    public RequestDTO getRequestDTOById(Long requestId) {
//?       Optional<Request> request = requestRepository.findById(requestId);
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request with ID: " + requestId + " not found"));
        return requestConvert.convertRequestDTO(request);
    }

    public List<RequestDTO> getAllRequestDTO() {
        List<Request> request = requestRepository.findAll();
        return request.stream().map(requestConvert::convertRequestDTO).collect(Collectors.toList());
    }

    public void deleteRequest(Long requestId) {
        requestRepository.deleteById(requestId);
    }

    private User getUser(RequestDTO requestDto) {
        Optional<User> optionalUser = userRepository.findByEmail(requestDto.getUserDTO().getEmail());
        User user;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            user = new User();
            user.setUserName(requestDto.getUserDTO().getUserName());
            user.setEmail(requestDto.getUserDTO().getEmail());
            user.setPhone(requestDto.getUserDTO().getPhone());
            user = userRepository.save(user);
        }
        return user;
    }

    private void validateRequest(RequestDTO requestDto) {
        if (Objects.isNull(requestDto.getDescription()) || requestDto.getDescription().trim().isEmpty()) {
            throw new MissingDescriptionException("Description is required.");
        }

        if (requestDto.getDescription().length() < 10 || requestDto.getDescription().length() > 500) {
            throw new MissingDescriptionLengthException("Description must be between 10 and 500 characters.");
        }

        if (!RegEx.USERNAME_REGEX.matcher(requestDto.getUserDTO().getUserName()).matches()) {
            throw new MissingUserNameException("User name format is invalid.");
        }

        if (Objects.isNull(requestDto.getUserDTO().getEmail()) || !RegEx.EMAIL_REGEX.matcher(requestDto.getUserDTO().getEmail()).matches()) {
            throw new InvalidEmailException("Invalid email format.");
        }

        if (Objects.isNull(requestDto.getUserDTO().getPhone()) || !RegEx.PHONE_REGEX.matcher(requestDto.getUserDTO().getPhone()).matches()) {
            throw new InvalidPhoneNumberException("Invalid phone number format.");
        }
    }
}
