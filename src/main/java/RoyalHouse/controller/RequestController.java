package RoyalHouse.controller;

import RoyalHouse.dto.RequestDTO;
import RoyalHouse.service.RequestService;
import RoyalHouse.util.Exception.InvalidEmailException;
import RoyalHouse.util.Exception.InvalidPhoneNumberException;
import RoyalHouse.util.Exception.MissingDescriptionException;
import RoyalHouse.util.Exception.MissingUserNameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/requests")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ResponseEntity<RequestDTO> createRequest(@RequestBody RequestDTO requestDto) {
        try {
            requestService.createRequest(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(requestDto);
        } catch (MissingDescriptionException | MissingUserNameException | InvalidEmailException | InvalidPhoneNumberException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestDTO> getRequestById(@PathVariable Long id) {
        try {
            RequestDTO requestDto = requestService.getRequestDTOById(id);
            return ResponseEntity.ok(requestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        try {
            requestService.deleteRequest(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
