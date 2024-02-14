package app.estateagency.services;

import app.estateagency.dto.request.OfferRequest;
import app.estateagency.dto.response.Response;
import app.estateagency.jpa.entities.Estate;
import app.estateagency.jpa.entities.Offer;
import app.estateagency.jpa.repositories.OfferRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * A service allowing to handle business logic related to Offer entities
 */
@Service
@RequiredArgsConstructor
public class OfferService {
    private final OfferRepository offerRepository;
    private final EstateService estateService;

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
