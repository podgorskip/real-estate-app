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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 *  A controller handling requests regarding Estate entities
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EstateController {
    private final EstateService estateService;

    /**
     * Allows owners to report an estate
     * @param userDetails Details of the user sending the request, used to validate required privilege
     * @param estateRequest Details of the reported estate
     * @return Response if successfully reported the estate
     */
    @RequiredPrivilege(Privilege.REPORT_OFFER)
    @PostMapping("/owner/report-offer")
    public ResponseEntity<Response> reportOffer(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody EstateRequest estateRequest) {
        Response response = estateService.reportEstate(userDetails.getUsername(), estateRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Allows agents to check reported estates to which both documents and photos are attached
     * @param userDetails Details of the user sending the request, used to validate required privilege
     * @return Response containing list of estates if present
     */
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

    /**
     * Allows users to check available estates with filters set
     * @return Response containing list of estates if present
     */
    @GetMapping("/auth/estates")
    public ResponseEntity<List<EstateResponse>> filterEstates(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "bathrooms", required = false) Integer bathrooms,
            @RequestParam(value = "rooms", required = false) Integer rooms,
            @RequestParam(value = "garage", required = false) Boolean garage,
            @RequestParam(value = "storey", required = false) Integer storey,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "balcony", required = false) Boolean balcony,
            @RequestParam(value = "availability", required = false) String availability,
            @RequestParam(value = "size", required = false) Double size,
            @RequestParam(value = "condition", required = false) String condition,
            @RequestParam(value = "priceFrom", required = false) Double priceFrom,
            @RequestParam(value = "priceTo", required = false) Double priceTo,
            @RequestParam(value = "postFrom", required = false) LocalDateTime postFrom,
            @RequestParam(value = "postTo", required = false) LocalDateTime postTo
    ) {
        Optional<List<Estate>> optionalEstates = estateService.getFilteredEstates(
                bathrooms, rooms, garage, storey, location, balcony, size,
                condition, type, availability, priceFrom, priceTo, postFrom, postTo);

        return optionalEstates
                .map(estates -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(estates.stream().map(Mapper.INSTANCE::convertEstate).toList()))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }
}
