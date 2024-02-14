package app.estateagency.jpa.repositories;

import app.estateagency.jpa.entities.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    @Query( "SELECT a FROM Agent a " +
            "JOIN User u ON u.id = a.user.id " +
            "WHERE u.username = :username")
    Optional<Agent> findByUsername(@Param("username") String username);
}
