package app.estateagency.controllers;

import app.estateagency.dto.request.UserRequest;
import app.estateagency.dto.response.Response;
import app.estateagency.enums.Privilege;
import app.estateagency.security.RequiredPrivilege;
import app.estateagency.services.AdminService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  A controller handling requests regarding Admin entity
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    /**
     * Allows to add a new admin account
     * @param userDetails Details of the user who requests creating the new account
     * @param userRequest Details of the newly created user
     * @return Response if successfully add the account
     */
    @RequiredPrivilege(Privilege.ADD_ADMIN)
    @PostMapping("/admin/add-admin")
    public ResponseEntity<Response> createAdminAccount(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody UserRequest userRequest) {
        Response response = adminService.createAdminAccount(userRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
