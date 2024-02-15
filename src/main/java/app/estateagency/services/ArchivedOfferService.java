package app.estateagency.services;

import app.estateagency.jpa.entities.HistoryOffer;
import app.estateagency.jpa.repositories.HistoryOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * A service allowing to handle business logic related to offers history
 */
@Service
@RequiredArgsConstructor
public class HistoryOfferService {
    private final HistoryOfferRepository historyOfferRepository;

    /**
     * Retrieves offer history by its
     * @param id
     * @return
     */
    public Optional<HistoryOffer> getByID(Long id) {
        return historyOfferRepository.findById(id);
    }
}
