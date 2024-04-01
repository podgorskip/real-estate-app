package app.estateagency.dto.response;

import app.estateagency.enums.Role;
import lombok.Data;

@Data
public class UserDetailsResponse {
    private Long id;
    private String username;
    private Role role;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
