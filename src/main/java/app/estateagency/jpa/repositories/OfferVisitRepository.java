package app.estateagency.jpa.repositories;

import app.estateagency.jpa.entities.OfferVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Handles database operations related to OfferVisit entities
 */
@Repository
public interface OfferVisitRepository extends JpaRepository<OfferVisit, Long> {
}
