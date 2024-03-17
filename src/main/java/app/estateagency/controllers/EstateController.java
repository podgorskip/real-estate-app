package app.estateagency.controllers;

import app.estateagency.dto.Mapper;
import app.estateagency.dto.request.EstateRequest;
import app.estateagency.dto.response.EstateResponse;
import app.estateagency.dto.response.Response;
import app.estateagency.enums.Privilege;
import app.estateagency.jpa.entities.Estate;
import app.estateagency.security.RequiredPrivilege;
import app.estateagency.services.EstateService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<Long> reportOffer(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody EstateRequest estateRequest) {
        Optional<Long> id = estateService.reportEstate(userDetails.getUsername(), estateRequest);
        return id.map(aLong -> ResponseEntity.status(HttpStatus.CREATED).body(aLong)).orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
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
     * Allows owners to check their reported estates
     * @param userDetails Details of the owner who checks the estates
     * @return Response containing list of estates if present
     */
    @RequiredPrivilege(Privilege.REPORT_OFFER)
    @GetMapping("/owner/my-estates")
    public ResponseEntity<List<EstateResponse>> checkMyEstates(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<List<Estate>> optionalEstates = estateService.getByOwnerUsername(userDetails.getUsername());

        return optionalEstates.map(estates -> ResponseEntity
                .status(HttpStatus.OK)
                .body(estates.stream().map(Mapper.INSTANCE::convertEstate).toList()))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }
}
