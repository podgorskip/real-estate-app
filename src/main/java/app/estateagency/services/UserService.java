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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Response createUserAccount(UserRequest userRequest, Role role) {
        Optional<User> user = createUser(userRequest, role);

        if (user.isEmpty()) {
            return new Response(false, HttpStatus.CONFLICT, "Username is already taken");
        }

        userRepository.save(user.get());

        return new Response(true, HttpStatus.OK, "Correctly created the account");
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Response updateCredentials(String username, CredentialsUpdateRequest credentialsUpdateRequest) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No account of the specified username found");

        if (!credentialsUpdateRequest.getUsername().isBlank() && isUsernameUnavailable(credentialsUpdateRequest.getUsername()))
            return new Response(false, HttpStatus.CONFLICT, "Username is already taken");

        User updateUser = setUpdatedAttributes(user.get(), credentialsUpdateRequest);

        userRepository.save(updateUser);

        return new Response(true, HttpStatus.OK, "Successfully updated credentials");
    }

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

    private boolean isUsernameUnavailable(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        return optionalUser.isPresent();
    }

    private User setUpdatedAttributes(User user, CredentialsUpdateRequest credentialsUpdateRequest) {
        if (!credentialsUpdateRequest.getFirstName().isBlank())
            user.setUsername(credentialsUpdateRequest.getFirstName());

        if (!credentialsUpdateRequest.getLastName().isBlank())
            user.setLastName(credentialsUpdateRequest.getLastName());

        if (!credentialsUpdateRequest.getEmail().isBlank())
            user.setEmail(credentialsUpdateRequest.getEmail());

        if (!credentialsUpdateRequest.getPhoneNumber().isBlank())
            user.setPhoneNumber(credentialsUpdateRequest.getPhoneNumber());

        return user;
    }

    private Response validatePassword(User user, PasswordUpdateRequest passwordUpdateRequest) {
        if (!passwordEncoder.matches(passwordUpdateRequest.getOldPassword(), user.getPassword()))
            return new Response(false, HttpStatus.BAD_REQUEST, "Provided password doesn't match the current one");

        if (passwordEncoder.matches(passwordUpdateRequest.getNewPassword(), user.getPassword()))
            return new Response(false, HttpStatus.BAD_REQUEST, "New password cannot be the same as the old one");

        return new Response(true, HttpStatus.OK, "Passwords are valid");
    }
}
