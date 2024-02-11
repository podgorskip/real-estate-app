package app.estateagency.services;

import app.estateagency.dto.request.UserRequest;
import app.estateagency.dto.response.Response;
import app.estateagency.enums.Role;
import app.estateagency.jpa.entities.Owner;
import app.estateagency.jpa.repositories.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final UserService userService;

    public Response createOwnerAccount(UserRequest userRequest) {
        Response response = userService.createUserAccount(userRequest, Role.OWNER);

        if (!response.isSuccess())
            return response;

        Owner owner = new Owner();
        owner.setUser(userService.getByUsername(userRequest.getUsername()).get());

        ownerRepository.save(owner);

        return new Response(true, HttpStatus.CREATED, "Successfully created owner account");
    }
}
