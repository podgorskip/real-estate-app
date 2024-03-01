package app.estateagency.services;

import app.estateagency.dto.request.UserRequest;
import app.estateagency.dto.response.Response;
import app.estateagency.enums.Role;
import app.estateagency.jpa.entities.Customer;
import app.estateagency.jpa.repositories.CustomerRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * A service allowing to handle business logic related to Customer entities
 */
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserService userService;

    /**
     * Allows to create a Customer account
     * @param userRequest Details of the customer whose account is created
     * @return Response if successfully created the account
     */
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

    /**
     * Retrieves Customer object by their ID
     * @param id ID of the customer
     * @return Customer object if present, empty otherwise
     */
    public Optional<Customer> getByID(Long id) {
        return customerRepository.findById(id);
    }

    /**
     * Retrieves Customer object by their username
     * @param username Username of the customer
     * @return Customer object if present, empty otherwise
     */
    public Optional<Customer> getByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

}
