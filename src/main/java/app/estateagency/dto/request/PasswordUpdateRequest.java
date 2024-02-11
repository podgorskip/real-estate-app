package app.estateagency.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordUpdateRequest {
    @NotBlank(message = "Old password cannot be blank")
    @Size(max = 100, message = "Old password cannot be longer than 100 characters")
    private String oldPassword;

    @NotBlank(message = "Password cannot be blank")
    @Size(max = 100, message = "Password cannot be longer than 100 characters")
    private String newPassword;
}
