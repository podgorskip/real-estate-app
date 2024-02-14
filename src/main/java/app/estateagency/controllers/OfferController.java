package app.estateagency.controllers;

import app.estateagency.dto.request.OfferRequest;
import app.estateagency.dto.response.Response;
import app.estateagency.enums.Privilege;
import app.estateagency.security.RequiredPrivilege;
import app.estateagency.services.OfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
