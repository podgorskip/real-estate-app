package app.estateagency.controllers;

import app.estateagency.dto.Mapper;
import app.estateagency.dto.request.OfferRequest;
import app.estateagency.dto.response.OfferResponse;
import app.estateagency.dto.response.Response;
import app.estateagency.enums.Privilege;
import app.estateagency.jpa.entities.Offer;
import app.estateagency.security.RequiredPrivilege;
import app.estateagency.services.OfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 *  A controller handling requests regarding Offer entities
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OfferController {
    private final OfferService offerService;

    /**
     * Allows agents to post offers for estates.
     * @param userDetails User details of the user who makes request, used to validate required privilege
     * @param id ID of the estate of which the offer is related
     * @param offerRequest Details of the offer being posted
     * @return Response indicating the success or failure of posting the offer
     */
    @RequiredPrivilege(Privilege.ADD_OFFER)
    @PostMapping("/agent/post-offer")
    public ResponseEntity<Response> postOffer(@AuthenticationPrincipal UserDetails userDetails, @Param("id") Long id, @Valid @RequestBody OfferRequest offerRequest) {
        Response response = offerService.postOffer(id, offerRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Allows users to check available offers with filters set
     * @return Response containing list of offers if present
     */
    @RequiredPrivilege(Privilege.CHECK_OFFERS)
    @GetMapping("/auth/offers")
    public ResponseEntity<List<OfferResponse>> filterEstates(
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
        Optional<List<Offer>> optionalOffers = offerService.getFilteredOffers(
                bathrooms, rooms, garage, storey, location, balcony, size,
                condition, type, availability, priceFrom, priceTo, postFrom, postTo);

        return optionalOffers
                .map(estates -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(estates.stream().map(Mapper.INSTANCE::convertOffer).toList()))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }
}
