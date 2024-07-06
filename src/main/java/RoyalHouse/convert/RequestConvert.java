package RoyalHouse.convert;

import RoyalHouse.dto.RequestDTO;
import RoyalHouse.dto.UserDTO;
import RoyalHouse.model.Request;
import org.springframework.stereotype.Component;

@Component
public class RequestConvert {
    private final UserConvert userConvert;

    public RequestConvert(UserConvert userConvert) {
        this.userConvert = userConvert;
    }

    public RequestDTO convertRequestDTO(Request request) {
        UserDTO userDTO = userConvert.convertToUserDTO(request.getUser());
        return new RequestDTO(request, userDTO);
    }
}
