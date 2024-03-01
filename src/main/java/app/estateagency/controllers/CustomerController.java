package app.estateagency.controllers;

import app.estateagency.dto.request.UserRequest;
import app.estateagency.dto.response.Response;
import app.estateagency.services.CustomerService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  A controller handling requests regarding Customer entities
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    /**
     * Allows to register as a customer
     * @param userRequest Details of the user who created the account
     * @return Response if successfully created the account
     */
    @PostMapping("/auth/customer/register")
    public ResponseEntity<Response> registerCustomer(@Valid @RequestBody UserRequest userRequest) {
        Response response = customerService.createCustomerAccount(userRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
