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
import jakarta.validation.constraints.NotNull;
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

    /**
     * Allows customers to block offers
     * @param userDetails User details of the customer who blocks the offer
     * @param id ID of the offer to be blocked
     * @return Response if successfully blocked the offer
     */
    @RequiredPrivilege(Privilege.BLOCK_OFFER)
    @PatchMapping("/customer/block-offer")
    public ResponseEntity<Response> blockOffer(@AuthenticationPrincipal UserDetails userDetails, @NotNull @RequestParam("id") Long id) {
        Response response = offerService.blockOffer(userDetails.getUsername(), id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Allows agents and customers to unblock offers
     * @param userDetails User details of the agent/customer who unblocks the offer
     * @param id ID of the offer to be unblocked
     * @return Response if successfully unblocked the offer
     */
    @RequiredPrivilege(Privilege.UNBLOCK_OFFER)
    @PatchMapping("/unblock-offer")
    public ResponseEntity<Response> unblockOffer(@AuthenticationPrincipal UserDetails userDetails, @NotNull @RequestParam("id") Long id) {
        Response response = offerService.unblockOffer(userDetails.getUsername(), id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Retrieves offers waiting to be finalized
     * @param userDetails User details of the agent requesting offers
     * @return List of offers if present, empty otherwise
     */
    @RequiredPrivilege(Privilege.CHECK_BLOCKED_OFFERS)
    @GetMapping("/agent/to-finalize")
    public ResponseEntity<List<OfferResponse>> checkOffersToFinalize(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<List<Offer>> optionalOffers = offerService.getBlockedOffersByAgentUsername(userDetails.getUsername());

        return optionalOffers
                .map(offers -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(offers.stream().map(Mapper.INSTANCE::convertOffer).toList()))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }

    /**
     * Retrieves offers blocked by the customer
     * @param userDetails User details of the customer making the request
     * @return List of blocked offers if present, empty otherwise
     */
    @RequiredPrivilege(Privilege.CHECK_BLOCKED_OFFERS)
    @GetMapping("/customer/blocked-offers")
    public ResponseEntity<List<OfferResponse>> checkMyBlockedOffers(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<List<Offer>> optionalOffers = offerService.getBlockedOffersByCustomerUsername(userDetails.getUsername());

        return optionalOffers
                .map(offers -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(offers.stream().map(Mapper.INSTANCE::convertOffer).toList()))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }

    /**
     * Allows agents to finalize offers moving them to archived offers
     * @param userDetails User details of the agent finalizing the offer
     * @param id ID of the offer to be finalized
     * @return Response if successfully finalized the offer
     */
    @RequiredPrivilege(Privilege.FINALIZE_OFFER)
    @PostMapping("/agent/finalize-offer")
    public ResponseEntity<Response> finalizeOffer(@AuthenticationPrincipal UserDetails userDetails, @NotNull @RequestParam("id") Long id) {
        Response response = offerService.finalizeOffer(userDetails.getUsername(), id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Allows customers to add offers to favorites
     * @param userDetails User details of the customer making the request
     * @param id ID of the offer being added to favorites
     * @return Response if successfully liked the offer
     */
    @RequiredPrivilege(Privilege.ADD_TO_FAVORITES)
    @PostMapping("/customer/add-to-favorites")
    public ResponseEntity<Response> addToFavorites(@AuthenticationPrincipal UserDetails userDetails, @NotNull @RequestParam("id") Long id) {
        Response response = offerService.addOfferToFavorites(userDetails.getUsername(), id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Allows customers to check offers they added to favorites
     * @param userDetails User details of the customer making the request
     * @return List of offers if present, empty otherwise
     */
    @RequiredPrivilege(Privilege.CHECK_FAVORITES)
    @GetMapping("/customer/favorites")
    public ResponseEntity<List<OfferResponse>> checkFavorites(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<List<Offer>> optionalOffers = offerService.getFavoriteOffers(userDetails.getUsername());

        return optionalOffers
                .map(offers -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(offers.stream().map(Mapper.INSTANCE::convertOffer).toList()))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }

    /**
     * Allows customers to remove offers from favorites
     * @param userDetails User details of the customer making the request
     * @param id ID of the offer to be removed from favorites
     * @return Response if successfully removed the offer from favorites
     */
    @RequiredPrivilege(Privilege.REMOVE_FROM_FAVORITES)
    @PostMapping("/customer/remove-from-favorites")
    public ResponseEntity<Response> removeFromFavorites(@AuthenticationPrincipal UserDetails userDetails, @NotNull @RequestParam("id") Long id) {
        Response response = offerService.removeOfferFromFavorites(userDetails.getUsername(), id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
