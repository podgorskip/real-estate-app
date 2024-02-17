package app.estateagency.services;

import app.estateagency.dto.request.OfferRequest;
import app.estateagency.dto.response.Response;
import app.estateagency.jpa.entities.Customer;
import app.estateagency.jpa.entities.Estate;
import app.estateagency.jpa.entities.Offer;
import app.estateagency.jpa.repositories.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * A service allowing to handle business logic related to Offer entities
 */
@Service
@RequiredArgsConstructor
public class OfferService {
    private final OfferRepository offerRepository;
    private final EstateService estateService;
    private final CustomerService customerService;
    private final ArchivedOfferService archivedOfferService;
    private final OfferVisitService offerVisitService;

    /**
     * Allows an agent to post an offer for the estate owners reported.
     * @param id ID of the estate of which the offer is posted
     * @param offerRequest Details of the offer being posted
     * @return Response containing info is successfully posted or what when wrong
     */
    @Transactional
    public Response postOffer(Long id, OfferRequest offerRequest) {
        Optional<Estate> estate = estateService.getByID(id);

        if (estate.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "NO estate of the provided ID found");

        offerRepository.save(createOffer(estate.get(), offerRequest));

        return new Response(true, HttpStatus.CREATED, "Successfully posted the offer");
    }

    public Optional<List<Offer>> getFilteredOffers(Integer bathrooms, Integer rooms, Boolean garage, Integer storey,
                                                   String location, Boolean balcony, Double size, String condition,
                                                   String type, String availability, Double priceFrom, Double priceTo,
                                                   LocalDateTime postFrom, LocalDateTime postTo) {

        Optional<List<Estate>> optionalEstates = estateService.getFilteredEstates(bathrooms, rooms, garage, storey, location,
                                                                          balcony, size, condition, type, availability,
                                                                          priceFrom, priceTo, postFrom, postTo);

        return optionalEstates.map(estates -> estates.stream().map(offerRepository::findByEstate).toList());
    }

    /**
     * Allows to block offer for a customer
     * @param username Username of the customer who requires the block
     * @param id ID of the offer to be blocked
     * @return Response if successfully blocked the offer
     */
    @Transactional
    public Response blockOffer(String username, Long id) {
        Optional<Offer> optionalOffer = offerRepository.findById(id);

        if (optionalOffer.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No offer of the provided ID found");

        Optional<Customer> customer = customerService.getByUsername(username);

        if (customer.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No customer of the provided username found");

        Offer offer = optionalOffer.get();

        offer.setBlocked(true);
        offer.setBlockedBy(customer.get());

        return new Response(true, HttpStatus.OK, "Successfully blocked the offer");
    }

    /**
     * Allows agents and customers to unblock offers
     * @param username Username of the agent/customer who unblocks the offer
     * @param id ID of the offer to be unblocked
     * @return Response if successfully unblocked the offer
     */
    @Transactional
    public Response unblockOffer(String username, Long id) {
        Optional<Offer> optionalOffer = offerRepository.findById(id);

        if (optionalOffer.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No offer of the provided ID found");

        Offer offer = optionalOffer.get();

        if (!offer.getEstate().getAgent().getUser().getUsername().equals(username)  && !offer.getBlockedBy().getUser().getUsername().equals(username)) {
            return new Response(false, HttpStatus.BAD_REQUEST, "You are not authorized to unblock this offer");
        }

        offer.setBlocked(false);
        offer.setBlockedBy(null);

        offerRepository.save(offer);

        return new Response(true, HttpStatus.OK, "Successfully unblocked the offer");
    }

    /**
     * Retrieves blocked offers for a specified agent
     * @param username Username of the agent
     * @return List of blocked offers if present, empty otherwise
     */
    public Optional<List<Offer>> getBlockedOffersByAgentUsername(String username) {
        return offerRepository.findBlockedOffersByAgentUsername(username);
    }

    /**
     * Allows agents to finalize blocked offers
     * @param username Username of the agent finalizing the offer
     * @param id ID of the finalized offer
     * @return Response if successfully finalized the offer
     */
    @Transactional
    public Response finalizeOffer(String username, Long id) {
        Optional<Offer> optionalOffer = offerRepository.findById(id);
        Response response = validateFinalizeRequest(optionalOffer, username);

        if (!response.isSuccess())
            return response;

        Offer offer = optionalOffer.get();

        archivedOfferService.archiveOffer(offer);
        offerRepository.delete(offer);

        return new Response(true, HttpStatus.OK, "Successfully finalized the offer");
    }

    /**
     * Retrieves blocked offers by the specified customer
     * @param username Username of the customer whose blocked offers are retrieved
     * @return List of offers if present, empty otherwise
     */
    public Optional<List<Offer>> getBlockedOffersByCustomerUsername(String username) {
        return offerRepository.findBlockedOffersByCustomerUsername(username);
    }

    /**
     * Allows customers to add an offer to favorites
     * @param username Username of the customer who likes the offer
     * @param id ID of the offer tp be liked
     * @return Response if successfully added the offer to favorites
     */
    public Response addOfferToFavorites(String username, Long id) {
        Optional<Customer> customer = customerService.getByUsername(username);

        if (customer.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No customer of the provided username found");

        Optional<Offer> offer = offerRepository.findById(id);

        if (offer.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No offer of the provided ID found");

        offer.get().getCustomers().add(customer.get());
        offerRepository.save(offer.get());

        return new Response(true,  HttpStatus.CREATED, "Successfully added the offer to favorites");
    }

    /**
     * Retrieves offers added to favorites
     * @param username Username of the customer whose liked offers are retrieved
     * @return List of offers if present, empty otherwise
     */
    public Optional<List<Offer>> getFavoriteOffers(String username) {
        Optional<Customer> optionalCustomer = customerService.getByUsername(username);

        return optionalCustomer.map(customer -> customer.getLikes().stream().toList());
    }

    /**
     * Allows customers to remove offers from favorites
     * @param username Username of the customer who wants to unlike an offer
     * @param id ID of the offer to be unliked
     * @return Response if successfully unliked the offer
     */
    public Response removeOfferFromFavorites(String username, Long id) {
        Optional<Customer> customer = customerService.getByUsername(username);

        if (customer.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No customer of the provided username found");

        Optional<Offer> offer = offerRepository.findById(id);

        if (offer.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No offer of the provided username found");

        offer.get().getCustomers().remove(customer.get());
        offerRepository.save(offer.get());

        return new Response(true, HttpStatus.OK, "Successfully removed the offer from favorites");
    }

    /**
     * Retrieves the specified visit marking it as visited by the customer
     * @param username Username of the customer who checks the offer
     * @param id ID of the offer which is checked
     * @return Offer if present, empty otherwise
     */
    public Optional<Offer> checkOffer(String username, Long id) {
        Optional<Offer> offer = offerRepository.findById(id);
        Optional<Customer> customer = customerService.getByUsername(username);

        if (offer.isEmpty() || customer.isEmpty())
            return Optional.empty();

        offerVisitService.addOfferVisit(customer.get(), offer.get());

        return offer;
    }

    /**
     * Allows to check if finalize request is valid
     * @param offer Offer which is to be finalized
     * @param username Username of the agent finalizing the offer
     * @return Response if request is valid
     */
    private Response validateFinalizeRequest(Optional<Offer> offer, String username) {
        if (offer.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No offer of the provided ID found");

        if (!offer.get().isBlocked())
            return new Response(false, HttpStatus.BAD_REQUEST, "The offer was not blocked by any customer");

        if (!offer.get().getEstate().getAgent().getUser().getUsername().equals(username))
            return new Response(false, HttpStatus.BAD_REQUEST, "The offer is not assigned to the agent of the provided username");

        return new Response(true, HttpStatus.OK, "Finalize request is valid");
    }

    /**
     * Allows to create a fully populated offer entity
     * @param estate Estate of which the offer is related
     * @param offerRequest Details of the offer being posted
     * @return Fully populated offer entity
     */
    private Offer createOffer(Estate estate, OfferRequest offerRequest) {
        Offer offer = new Offer();

        offer.setDescription(offerRequest.getDescription());
        offer.setPrice(offerRequest.getPrice());
        offer.setEstate(estate);

        return offer;
    }

}
