package app.estateagency.services;

import app.estateagency.dto.request.UserRequest;
import app.estateagency.dto.response.Response;
import app.estateagency.enums.Role;
import app.estateagency.jpa.entities.Admin;
import app.estateagency.jpa.repositories.AdminRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * A service allowing to handle business logic related to Admin entities
 */
@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final UserService userService;

    /**
     * Allows to create an Admin account
     * @param userRequest Details of the admin whose account is created
     * @return Response if successfully created the account
     */
    @Transactional
    public Response createAdminAccount(UserRequest userRequest) {
        Response response = userService.createUserAccount(userRequest, Role.ADMIN);

        if (!response.isSuccess())
            return response;

        Admin admin = new Admin();
        admin.setUser(userService.getByUsername(userRequest.getUsername()).get());

        adminRepository.save(admin);

        return new Response(true, HttpStatus.CREATED, "Successfully created admin account");
    }
}
