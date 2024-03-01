package app.estateagency.jpa.repositories;

import app.estateagency.jpa.entities.CreditworthinessInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Handles database operations related to CreditworthinessInfo entities
 */
@Repository
public interface CreditworthinessInfoRepository extends JpaRepository<CreditworthinessInfo, Long> {
}
