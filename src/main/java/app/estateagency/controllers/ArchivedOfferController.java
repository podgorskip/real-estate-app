package app.estateagency.controllers;

import app.estateagency.dto.Mapper;
import app.estateagency.dto.response.ArchivedOfferResponse;
import app.estateagency.enums.Privilege;
import app.estateagency.jpa.entities.ArchivedOffer;
import app.estateagency.security.RequiredPrivilege;
import app.estateagency.services.ArchivedOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

/**
 *  A controller handling requests regarding ArchivedOffer entity
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ArchivedOfferController {
    private final ArchivedOfferService archivedOfferService;

    /**
     * Retrieves archived offers by customer's username
     * @param userDetails User details of the customer making the request
     * @return List of archived offers if present, empty otherwise§1
     */
    @RequiredPrivilege(Privilege.CHECK_ARCHIVED_OFFERS)
    @GetMapping("/customer/archived-offers")
    public ResponseEntity<List<ArchivedOfferResponse>> checkArchivedOffersByCustomer(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<List<ArchivedOffer>> optionalArchivedOffers = archivedOfferService.getCustomerUnreviewedArchivedOffers(userDetails.getUsername());

        return optionalArchivedOffers
                .map(archivedOffers -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(archivedOffers.stream().map(Mapper.INSTANCE::convertArchivedOffer).toList()))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }

    /**
     * Retrieves archived offers by owner's username
     * @param userDetails User details of the owner making the request
     * @return List of archived offers if present, empty otherwise
     */
    @RequiredPrivilege(Privilege.CHECK_ARCHIVED_OFFERS)
    @GetMapping("/owner/archived-offers")
    public ResponseEntity<List<ArchivedOfferResponse>> checkArchivedOffersByOwner(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<List<ArchivedOffer>> optionalArchivedOffers = archivedOfferService.getOwnerUnreviewedArchivedOffers(userDetails.getUsername());

        return optionalArchivedOffers
                .map(archivedOffers -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(archivedOffers.stream().map(Mapper.INSTANCE::convertArchivedOffer).toList()))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }
}
