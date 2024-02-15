package app.estateagency.jpa.repositories;

import app.estateagency.jpa.entities.Estate;
import app.estateagency.jpa.entities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    Offer findByEstate(Estate estate);
}
