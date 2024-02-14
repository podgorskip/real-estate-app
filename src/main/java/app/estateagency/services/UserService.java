package app.estateagency.services;

import app.estateagency.dto.request.CredentialsUpdateRequest;
import app.estateagency.dto.request.PasswordUpdateRequest;
import app.estateagency.dto.request.UserRequest;
import app.estateagency.dto.response.Response;
import app.estateagency.enums.Role;
import app.estateagency.jpa.entities.User;
import app.estateagency.jpa.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.Optional;

/**
 *  A service allowing to handle business logic related to User entities
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Allows to create a User account
     * @param userRequest Details of the user whose account is created
     * @return Response if successfully created the account
     */
    @Transactional
    public Response createUserAccount(UserRequest userRequest, Role role) {
        Optional<User> user = createUser(userRequest, role);

        if (user.isEmpty()) {
            return new Response(false, HttpStatus.CONFLICT, "Username is already taken");
        }

        userRepository.save(user.get());

        return new Response(true, HttpStatus.OK, "Correctly created the account");
    }

    /**
     * Retrieves a user by their username
     * @param username Username of the user
     * @return User object if present, empty otherwise
     */
    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Allows users to update their credentials
     * @param username Username of the user to have their credentials updated
     * @param credentialsUpdateRequest Details of the credentials change
     * @return Response if successfully updated credentials
     */
    public Response updateCredentials(String username, CredentialsUpdateRequest credentialsUpdateRequest) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No account of the specified username found");

        if (!credentialsUpdateRequest.getUsername().isBlank() && isUsernameUnavailable(credentialsUpdateRequest.getUsername()))
            return new Response(false, HttpStatus.CONFLICT, "Username is already taken");

        setUpdatedAttributes(user.get(), credentialsUpdateRequest);

        return new Response(true, HttpStatus.OK, "Successfully updated credentials");
    }

    /**
     * Allows users to update their password
     * @param username Username of the user to have their password updated
     * @param passwordUpdateRequest  Details of the password change
     * @return Response if successfully updated password
     */
    public Response updatePassword(String username, PasswordUpdateRequest passwordUpdateRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No account of the specified username found");

        User user = optionalUser.get();

        if (!validatePassword(user, passwordUpdateRequest).isSuccess())
            return new Response();

        user.setPassword(passwordEncoder.encode(passwordUpdateRequest.getNewPassword()));

        userRepository.save(user);

        return new Response(true, HttpStatus.OK, "Successfully updated password");
    }

    /**
     * Allows to create a fully populated User object
     * @param userRequest Details of the user
     * @param role Role which should be assigned to the user
     * @return Fully populated user object if successfully created, empty otherwise
     */
    private Optional<User> createUser(UserRequest userRequest, Role role) {

        if (isUsernameUnavailable(userRequest.getUsername()))
            return Optional.empty();

        User user = new User();

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setRole(role);

        return Optional.of(user);
    }

    /**
     * Allows to check if the provided username is unavailable
     * @param username Username to be checked
     * @return true if is unavailable, false otherwise
     */
    private boolean isUsernameUnavailable(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        return optionalUser.isPresent();
    }

    /**
     * Allows to set updated credentials
     * @param user User to have credentials updated
     * @param credentialsUpdateRequest Details of the credentials change
     */
    private void setUpdatedAttributes(User user, CredentialsUpdateRequest credentialsUpdateRequest) {
        if (Objects.nonNull(credentialsUpdateRequest.getFirstName()) && !credentialsUpdateRequest.getFirstName().isBlank())
            user.setFirstName(credentialsUpdateRequest.getFirstName());

        if (Objects.nonNull(credentialsUpdateRequest.getLastName()) && !credentialsUpdateRequest.getLastName().isBlank())
            user.setLastName(credentialsUpdateRequest.getLastName());

        if (Objects.nonNull(credentialsUpdateRequest.getEmail()) && !credentialsUpdateRequest.getEmail().isBlank())
            user.setEmail(credentialsUpdateRequest.getEmail());

        if (Objects.nonNull(credentialsUpdateRequest.getPhoneNumber()) && !credentialsUpdateRequest.getPhoneNumber().isBlank())
            user.setPhoneNumber(credentialsUpdateRequest.getPhoneNumber());

        if (Objects.nonNull(credentialsUpdateRequest.getUsername()) && !credentialsUpdateRequest.getUsername().isBlank())
            user.setUsername(credentialsUpdateRequest.getUsername());

        userRepository.save(user);
    }

    /**
     * Allows to validate passwords when they are updated
     * @param user User who requests password change
     * @param passwordUpdateRequest Details of the password change (old password, new password)
     * @return Response if passwords are valid
     */
    private Response validatePassword(User user, PasswordUpdateRequest passwordUpdateRequest) {
        if (!passwordEncoder.matches(passwordUpdateRequest.getOldPassword(), user.getPassword()))
            return new Response(false, HttpStatus.BAD_REQUEST, "Provided password doesn't match the current one");

        if (passwordEncoder.matches(passwordUpdateRequest.getNewPassword(), user.getPassword()))
            return new Response(false, HttpStatus.BAD_REQUEST, "New password cannot be the same as the old one");

        return new Response(true, HttpStatus.OK, "Passwords are valid");
    }
}
