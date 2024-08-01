package RoyalHouse.service.admin.setting;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordForm {

    @NotBlank(message = "Current password cannot be blank.")
    private String currentPassword;

    @NotBlank(message = "New password cannot be blank.")
    private String newPassword;

    @NotBlank(message = "Confirm password cannot be blank.")
    private String confirmPassword;

}