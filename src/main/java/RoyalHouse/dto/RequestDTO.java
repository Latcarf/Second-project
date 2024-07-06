package RoyalHouse.dto;

import RoyalHouse.model.Request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {
    private Long requestId;
    private String description;
    private String status;
    private LocalDateTime date;
    private UserDTO userDTO;

public RequestDTO(Request request, UserDTO userDTO) {
    this.requestId = request.getId();
    this.description = request.getDescription();
    this.status = request.getStatus();
    this.date = request.getDate();
    this.userDTO = userDTO;
}

}
