package app.estateagency.jpa.repositories;

import app.estateagency.jpa.entities.Agent;
import app.estateagency.jpa.entities.Estate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstateRepository extends JpaRepository<Estate, Long> {
    Optional<List<Estate>> findByAgent(Agent agent);

    @Query("SELECT e FROM Estate e " +
            "JOIN Document d ON d.estate = e " +
            "JOIN Photo p ON p.estate = e " +
            "LEFT JOIN Offer o ON o.estate = e " +
            "WHERE o IS NULL")
    Optional<List<Estate>> findReportedEstatesByAgent(@Param("agent") Agent agent);
}
