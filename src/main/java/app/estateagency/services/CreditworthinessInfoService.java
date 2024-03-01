package app.estateagency.services;

import app.estateagency.dto.request.CreditworthinessInfoRequest;
import app.estateagency.dto.response.Response;
import app.estateagency.enums.credit.EducationLevel;
import app.estateagency.enums.credit.EmploymentStatus;
import app.estateagency.enums.credit.Gender;
import app.estateagency.enums.credit.MaritalStatus;
import app.estateagency.jpa.entities.CreditworthinessInfo;
import app.estateagency.jpa.entities.Customer;
import app.estateagency.jpa.repositories.CreditworthinessInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

/**
 * A service allowing to handle business logic related to CreditworthinessInfo entities
 */
@Service
@RequiredArgsConstructor
public class CreditworthinessInfoService {
    private final CreditworthinessInfoRepository creditworthinessInfoRepository;
    private final CreditWorthinessAssessmentService creditWorthinessAssessmentService;
    private final CustomerService customerService;
    private final OfferService offerService;

    /**
     * Allows customers to fill in their creditworthiness info
     * @param username Username of the customer who fills in the info
     * @param creditWorthinessInfoRequest Details of the creditworthiness info
     * @return Response if successfully filled in the data
     */
    @Transactional
    public Response fillCreditWorthinessInfo(String username, CreditworthinessInfoRequest creditWorthinessInfoRequest) {
        Optional<Customer> customer = customerService.getByUsername(username);

        if (customer.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No customer of the provided username found");

        creditworthinessInfoRepository.save(createCreditworthinessInfo(customer.get(), creditWorthinessInfoRequest));

        return new Response(true, HttpStatus.CREATED, "Successfully filled in the credit worthiness info");
    }

    /**
     * Allows customers to check their creditworthiness
     * @param username Username of the customer to have the creditworthiness checked
     * @return Response with the info of the credibility
     */
    public Response checkCreditWorthiness(String username) {
        Optional<Customer> customer = customerService.getByUsername(username);

        if (customer.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No customer of the provided username found");

        if (customer.get().getCreditWorthinessInfo() == null)
            return new Response(false, HttpStatus.BAD_REQUEST, "No credit worthiness info assigned");

        Double creditworthiness = creditWorthinessAssessmentService.assess(customer.get().getCreditWorthinessInfo());

        if (creditworthiness.isNaN())
            return new Response(false, HttpStatus.BAD_REQUEST, "Not enough info to access creditworthiness");

        return new Response(true, HttpStatus.OK, creditworthiness.toString());
    }

    /**
     * Allows customers to check if their creditworthiness allows to buy the estate
     * @param username Username of the user to have the info checked
     * @param id ID of the estate to which creditworthiness is compared to
     * @return Response with the info of the credibility
     */
    public Response checkCreditWorthinessForOffer(String username, Long id) {
        Optional<Boolean> creditworthiness =  offerService.checkCreditWorthinessForEstatePrice(username, id);

        return creditworthiness
                .map(aBoolean -> new Response(true, HttpStatus.OK, aBoolean.toString()))
                .orElseGet(() -> new Response(false, HttpStatus.BAD_REQUEST, "No the specified customer or the offer found"));

    }

    /**
     * Allows to create a fully populated CreditworthinessInfo entity
     * @param customer Customer who fills in the data
     * @param creditWorthinessInfoRequest Details of the creditworthiness info
     * @return Fully populated CreditworthinessInfo entity
     */
    private CreditworthinessInfo createCreditworthinessInfo(Customer customer, CreditworthinessInfoRequest creditWorthinessInfoRequest) {
        CreditworthinessInfo creditWorthinessInfo = new CreditworthinessInfo();

        creditWorthinessInfo.setAge(creditWorthinessInfoRequest.getAge());
        creditWorthinessInfo.setCustomer(customer);
        creditWorthinessInfo.setDebts(creditWorthinessInfoRequest.getDebts());
        creditWorthinessInfo.setExpenses(creditWorthinessInfoRequest.getExpenses());
        creditWorthinessInfo.setGender(Gender.valueOf(creditWorthinessInfoRequest.getGender().toUpperCase()));
        creditWorthinessInfo.setIncome(creditWorthinessInfoRequest.getIncome());
        creditWorthinessInfo.setEmploymentStatus(EmploymentStatus.valueOf(creditWorthinessInfoRequest.getEmploymentStatus().toUpperCase()));
        creditWorthinessInfo.setMaritalStatus(MaritalStatus.valueOf(creditWorthinessInfoRequest.getMaritalStatus().toUpperCase()));
        creditWorthinessInfo.setEducationLevel(EducationLevel.valueOf(creditWorthinessInfoRequest.getEducationLevel().toUpperCase()));

        return creditWorthinessInfo;
    }
}
