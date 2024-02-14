package app.estateagency.controllers;

import app.estateagency.dto.request.UserRequest;
import app.estateagency.dto.response.Response;
import app.estateagency.enums.Privilege;
import app.estateagency.security.RequiredPrivilege;
import app.estateagency.services.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @RequiredPrivilege(Privilege.ADD_ADMIN)
    @PostMapping("/admin/add-admin")
    public ResponseEntity<Response> createAdminAccount(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody UserRequest userRequest) {
        Response response = adminService.createAdminAccount(userRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
