package app.estateagency.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {
    @NotBlank(message = "First name cannot be blank")
    @Size(max = 100, message = "First name cannot be longer than 100 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 100, message = "Last name cannot be longer than 100 characters")
    private String lastName;

    @NotBlank(message = "Username cannot be blank")
    @Size(max = 50, min = 5, message = "Username cannot be longer than 50 or shorter than 5 characters")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(max = 100, message = "Password cannot be longer than 100 characters")
    private String password;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Incorrect email format")
    @Size(max = 50, message = "Email cannot be longer than 50 characters")
    private String email;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "[0-9]{3} [0-9]{3} [0-9]{3}", message = "Phone number should align with format xxx xxx xxx")
    private String phoneNumber;
}
