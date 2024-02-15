package app.estateagency.services;

import app.estateagency.jpa.entities.ArchivedOffer;
import app.estateagency.jpa.entities.Offer;
import app.estateagency.jpa.repositories.ArchivedOfferRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * A service allowing to handle business logic related to offers history
 */
@Service
@RequiredArgsConstructor
public class ArchivedOfferService {
    private final ArchivedOfferRepository archivedOfferRepository;

    /**
     * Retrieves archived offers by their ID
     * @param id ID of the archived offer
     * @return ArchivedOffer object if present, empty otherwise
     */
    public Optional<ArchivedOffer> getByID(Long id) {
        return archivedOfferRepository.findById(id);
    }

    /**
     * Retrieves customer's unreviewed transactions
     * @param username Username of the user who retrieves their unreviewed transactions
     * @return List of archived offers if present, empty otherwise
     */
    public Optional<List<ArchivedOffer>> getCustomerUnreviewedArchivedOffers(String username) {
        return archivedOfferRepository.findCustomersUnreviewedArchivedOffers(username);
    }

    /**
     * Retrieves owner's unreviewed transactions
     * @param username Username of the user who retrieves their unreviewed transactions
     * @return List of archived offers if present, empty otherwise
     */
    public Optional<List<ArchivedOffer>> getOwnerUnreviewedArchivedOffers(String username) {
        return archivedOfferRepository.findOwnersUnreviewedArchivedOffers(username);
    }

    /**
     * Allows to archive offers
     * @param offer Offer to be archived
     */
    @Transactional
    public void archiveOffer(Offer offer) {
        ArchivedOffer archivedOffer = new ArchivedOffer();

        archivedOffer.setCustomer(offer.getBlockedBy());
        archivedOffer.setEstate(offer.getEstate());
        archivedOffer.setPrice(offer.getPrice());
        archivedOffer.setDescription(offer.getDescription());

        archivedOfferRepository.save(archivedOffer);
    }
}
