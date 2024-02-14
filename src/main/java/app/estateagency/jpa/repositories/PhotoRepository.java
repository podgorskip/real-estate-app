package app.estateagency.jpa.repositories;

import app.estateagency.jpa.entities.Estate;
import app.estateagency.jpa.entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Optional<List<Photo>> findByEstate(Estate estate);
}
