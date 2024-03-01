package app.estateagency.controllers;

import app.estateagency.dto.request.CredentialsUpdateRequest;
import app.estateagency.dto.request.PasswordUpdateRequest;
import app.estateagency.dto.request.UserRequest;
import app.estateagency.dto.response.Response;
import app.estateagency.enums.Privilege;
import app.estateagency.security.DatabaseUserDetails;
import app.estateagency.security.RequiredPrivilege;
import app.estateagency.services.CustomerService;
import app.estateagency.services.OwnerService;
import app.estateagency.services.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @RequiredPrivilege(Privilege.CHANGE_CREDENTIALS)
    @PatchMapping("/update-credentials")
    public ResponseEntity<Response> updateCredentials(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody CredentialsUpdateRequest credentialsUpdateRequest) {
        Response response = userService.updateCredentials(userDetails.getUsername(), credentialsUpdateRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @RequiredPrivilege(Privilege.CHANGE_PASSWORD)
    @PatchMapping("/update-password")
    public ResponseEntity<Response> updatePassword(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        Response response = userService.updatePassword(userDetails.getUsername(), passwordUpdateRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
