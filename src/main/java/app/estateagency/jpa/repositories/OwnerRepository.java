package app.estateagency.jpa.repositories;

import app.estateagency.jpa.entities.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    @Query( "SELECT o FROM Owner o " +
            "JOIN User u ON u.id = o.user.id " +
            "WHERE u.username = :username")
    Optional<Owner> findByUsername(@Param("username") String username);
}
