package RoyalHouse.convert;

import RoyalHouse.dto.UserDTO;
import RoyalHouse.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserConvert {
    public UserDTO convertToUserDTO(User user) {
        return new UserDTO(user);
    }

}
