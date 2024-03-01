package app.estateagency.controllers;

import app.estateagency.dto.request.CreditworthinessInfoRequest;
import app.estateagency.dto.response.Response;
import app.estateagency.enums.Privilege;
import app.estateagency.security.RequiredPrivilege;
import app.estateagency.services.CreditworthinessInfoService;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 *  A controller handling requests regarding CreditworthinessInfo entities
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CreditworthinessInfoController {
    private final CreditworthinessInfoService creditworthinessInfoService;

    /**
     * Allows customers to fill in their creditworthiness info
     * @param userDetails User details of the customer who sends the request
     * @param creditworthinessInfoRequest Details of the creditworthiness info
     * @return Response if successfully filled in the creditworthiness info
     */
    @RequiredPrivilege(Privilege.FILL_CREDITWORTHINESS_INFO)
    @PostMapping("/customer/fill-creditworthiness-info")
    public ResponseEntity<Response> fillCreditworthinessInfo(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody CreditworthinessInfoRequest creditworthinessInfoRequest) {
        Response response = creditworthinessInfoService.fillCreditWorthinessInfo(userDetails.getUsername(), creditworthinessInfoRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Allows to assess the customer's creditworthiness
     * @param userDetails User details of the customers who wants to have creditworthiness checked
     * @return Response containing the amount of creditworthiness if present
     */
    @RequiredPrivilege(Privilege.CHECK_CREDITWORTHINESS)
    @GetMapping("/customer/check-creditworthiness")
    public ResponseEntity<Response> checkCreditworthiness(@AuthenticationPrincipal UserDetails userDetails) {
        Response response = creditworthinessInfoService.checkCreditWorthiness(userDetails.getUsername());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Allows to access if the customer is credible enough for the credit which amount if the price of the offer
     * @param userDetails User details of the customers who wants to have creditworthiness checked
     * @param id ID of the offer to which the price is compared
     * @return Response containing info if the customer can get the credit
     */
    @RequiredPrivilege(Privilege.CHECK_CREDITWORTHINESS)
    @GetMapping("/customer/check-creditworthiness-offer")
    public ResponseEntity<Response> checkCreditworthinessForOffer(@AuthenticationPrincipal UserDetails userDetails, @NotNull @RequestParam("id") Long id) {
        Response response = creditworthinessInfoService.checkCreditWorthinessForOffer(userDetails.getUsername(), id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
