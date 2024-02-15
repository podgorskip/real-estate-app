package app.estateagency.jpa.repositories;

import app.estateagency.jpa.entities.HistoryOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Handles database operations related to HistoryOffer entities
 */
@Repository
public interface HistoryOfferRepository extends JpaRepository<HistoryOffer, Long> {
}
