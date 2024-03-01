package app.estateagency.controllers;

import app.estateagency.dto.request.UserRequest;
import app.estateagency.dto.response.Response;
import app.estateagency.services.OwnerService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerService ownerService;

    @PostMapping("/auth/owner/register")
    public ResponseEntity<Response> registerOwner(@Valid @RequestBody UserRequest userRequest) {
        Response response = ownerService.createOwnerAccount(userRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
