package app.estateagency.services;

import app.estateagency.dto.request.UserRequest;
import app.estateagency.dto.response.Response;
import app.estateagency.enums.Role;
import app.estateagency.jpa.entities.Customer;
import app.estateagency.jpa.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserService userService;

    @Transactional
    public Response createCustomerAccount(UserRequest userRequest) {

        Response response = userService.createUserAccount(userRequest, Role.CUSTOMER);

        if (!response.isSuccess())
            return response;

        Customer customer = new Customer();
        customer.setUser(userService.getByUsername(userRequest.getUsername()).get());

        customerRepository.save(customer);

        return new Response(true, HttpStatus.CREATED, "Successfully created customer account");
    }

}
