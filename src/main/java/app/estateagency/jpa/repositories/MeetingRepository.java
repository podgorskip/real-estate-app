package app.estateagency.jpa.repositories;

import app.estateagency.enums.Role;
import app.estateagency.jpa.entities.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Handles database operations related to Meeting entities
 */
@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    /**
     * Retrieves all the agent's meetings
     * @param username Username of the agent
     * @return List of meetings if present, empty otherwise
     */
    @Query("SELECT m FROM Meeting m " +
            "JOIN Agent a ON a = m.agent " +
            "JOIN User u ON u = a.user " +
            "WHERE u.username = :username")
    Optional<List<Meeting>> findByAgentUsername(@Param("username") String username);

    /**
     * Retrieves all the agent's meetings with users of the specified role
     * @param username Username of the agent
     * @param role Role of the users with whom meetings are scheduled
     * @return List of meetings if present, empty otherwise
     */
    @Query("SELECT m FROM Meeting m " +
            "JOIN Agent a ON a = m.agent " +
            "JOIN User u ON u = a.user " +
            "WHERE u.username = :username " +
            "AND m.role = :role")
    Optional<List<Meeting>> findByAgentUsernameAndRole(@Param("username") String username, @Param("role") Role role);

    /**
     * Retrieves meetings by username of the user who scheduled it
     * @param username Username of the user
     * @return List of meetings if present, empty otherwise
     */
    @Query("SELECT m FROM Meeting m " +
            "JOIN User u on u = m.user " +
            "WHERE u.username = :username")
    Optional<List<Meeting>> findByUsername(@Param("username") String username);
}
