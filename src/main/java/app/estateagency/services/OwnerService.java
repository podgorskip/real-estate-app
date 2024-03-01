package app.estateagency.services;

import app.estateagency.dto.request.UserRequest;
import app.estateagency.dto.response.Response;
import app.estateagency.enums.Role;
import app.estateagency.jpa.entities.Owner;
import app.estateagency.jpa.repositories.OwnerRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 *  A service allowing to handle business logic related to Owner entities
 */
@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final UserService userService;

    /**
     * Allows to create an Owner account
     * @param userRequest Details of the owner whose account is created
     * @return Response if successfully created the account
     */
    @Transactional
    public Response createOwnerAccount(UserRequest userRequest) {
        Response response = userService.createUserAccount(userRequest, Role.OWNER);

        if (!response.isSuccess())
            return response;

        Owner owner = new Owner();
        owner.setUser(userService.getByUsername(userRequest.getUsername()).get());

        ownerRepository.save(owner);

        return new Response(true, HttpStatus.CREATED, "Successfully created owner account");
    }

    /**
     * Retrieves an owner by their username
     * @param username Username of the owner
     * @return Owner object if present, empty otherwise
     */
    public Optional<Owner> getByUsername(String username) {
        return ownerRepository.findByUsername(username);
    }

    /**
     * Retrieves an owner by their ID
     * @param id ID of the owner
     * @return Owner object if present, empty otherwise
     */
    public Optional<Owner> getByID(Long id) {
        return ownerRepository.findById(id);
    }
}
