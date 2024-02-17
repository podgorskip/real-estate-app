package app.estateagency.services;

import app.estateagency.jpa.entities.Customer;
import app.estateagency.jpa.entities.Offer;
import app.estateagency.jpa.entities.OfferVisit;
import app.estateagency.jpa.repositories.OfferVisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * A service allowing to handle business logic related to OfferVisit entities
 */
@Service
@RequiredArgsConstructor
public class OfferVisitService {
    private final OfferVisitRepository offerVisitRepository;

    /**
     * Allows to mark offers as visited by the specified customer
     * @param customer Customer who visits the offer
     * @param offer Offer which is visited
     */
    @Transactional
    public void addOfferVisit(Customer customer, Offer offer) {
        OfferVisit offerVisit = new OfferVisit();

        offerVisit.setOffer(offer);
        offerVisit.setCustomer(customer);

        offerVisitRepository.save(offerVisit);
    }
}
