package app.estateagency.jpa.repositories;

import app.estateagency.jpa.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Handles database operations related to Review entities
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
