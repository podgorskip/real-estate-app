package app.estateagency.jpa.repositories;

import app.estateagency.jpa.entities.ArchivedOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Handles database operations related to HistoryOffer entities
 */
@Repository
public interface ArchivedOfferRepository extends JpaRepository<ArchivedOffer, Long> {
    /**
     * Retrieves archived unreviewed customer's transactions
     * @param username Username of the customer
     * @return List of archived offers to review if present, empty otherwise
     */
    @Query("SELECT a FROM ArchivedOffer a " +
            "LEFT JOIN Review r ON r.user = a.customer.user " +
            "WHERE a.customer.user.username = :username " +
            "AND r IS NULL")
    Optional<List<ArchivedOffer>> findCustomersUnreviewedArchivedOffers(@Param("username") String username);

    /**
     * Retrieves archived unreviewed owner's transactions
     * @param username Username of the owner
     * @return List of archived offers to review if present, empty otherwise
     */
    @Query("SELECT a FROM ArchivedOffer a " +
            "LEFT JOIN Review r ON r = a.review " +
            "WHERE a.estate.owner.user.username = :username " +
            "AND r IS NULL")
    Optional<List<ArchivedOffer>> findOwnersUnreviewedArchivedOffers(@Param("username") String username);
}
