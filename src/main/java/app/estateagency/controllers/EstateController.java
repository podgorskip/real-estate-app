package app.estateagency.controllers;

import app.estateagency.dto.Mapper;
import app.estateagency.dto.request.EstateRequest;
import app.estateagency.dto.response.EstateResponse;
import app.estateagency.dto.response.Response;
import app.estateagency.enums.Privilege;
import app.estateagency.jpa.entities.Estate;
import app.estateagency.security.RequiredPrivilege;
import app.estateagency.services.EstateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EstateController {
    private final EstateService estateService;

    @RequiredPrivilege(Privilege.REPORT_OFFER)
    @PostMapping("/owner/report-offer")
    public ResponseEntity<Response> reportOffer(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody EstateRequest estateRequest) {
        Response response = estateService.reportEstate(userDetails.getUsername(), estateRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @RequiredPrivilege(Privilege.CHECK_REPORTED_ESTATES)
    @GetMapping("/agent/reported-estates")
    public ResponseEntity<List<EstateResponse>> checkReportedEstates(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<List<Estate>> estates = estateService.getReportedEstatesByAgentUsername(userDetails.getUsername());

        return estates
                .map(estateList -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(estateList.stream().map(Mapper.INSTANCE::convertEstate).toList()))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }

    @GetMapping("/auth/estates")
    public ResponseEntity<List<EstateResponse>> checkEstates() {
        Optional<List<Estate>> optionalEstates = estateService.getAllEstates();

        return optionalEstates
                .map(estates -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(estates.stream().map(Mapper.INSTANCE::convertEstate).toList()))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }
}
