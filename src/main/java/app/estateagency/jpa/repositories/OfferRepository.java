package app.estateagency.jpa.repositories;

import app.estateagency.jpa.entities.Estate;
import app.estateagency.jpa.entities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Handles database operations related to Offer entities
 */
@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    /**
     * Retrieves the offer by the specified estate
     * @param estate Estate which is included in the offer
     * @return Offer object
     */
    Offer findByEstate(Estate estate);

    /**
     * Retrieves blocked offers by assigned agent
     * @param username Username of the agent assigned to the offer
     * @return List of blocked offers if present, empty otherwise
     */
    @Query("SELECT o FROM Offer o " +
            "WHERE o.blocked = TRUE " +
            "AND o.estate.agent.user.username = :username")
    Optional<List<Offer>> findBlockedOffersByAgentUsername(@Param("username") String username);

    /**
     * Retrieves blocked offers by the specified customer
     * @param username Username of the customer whose blocked offers are retrieved
     * @return List of blocked offers if present, empty otherwise
     */
    @Query("SELECT o FROM Offer o " +
            "WHERE o.blocked = TRUE " +
            "AND o.blockedBy.user.username = :username")
    Optional<List<Offer>> findBlockedOffersByCustomerUsername(@Param("username") String username);
}
