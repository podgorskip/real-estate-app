package app.estateagency.jpa.entities;

import app.estateagency.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_details", schema = "real_estate")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private boolean verified;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
