package app.estateagency.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CredentialsUpdateRequest {
    @Size(max = 100, message = "First name cannot be longer than 100 characters")
    private String firstName;

    @Size(max = 100, message = "Last name cannot be longer than 100 characters")
    private String lastName;

    @Size(max = 50, min = 5, message = "Username cannot be longer than 50 or shorter than 5 characters")
    private String username;

    @Email(message = "Incorrect email format")
    @Size(max = 50, message = "Email cannot be longer than 50 characters")
    private String email;

    @Pattern(regexp = "[0-9]{3} [0-9]{3} [0-9]{3}", message = "Phone number should align with format xxx xxx xxx")
    private String phoneNumber;
}
