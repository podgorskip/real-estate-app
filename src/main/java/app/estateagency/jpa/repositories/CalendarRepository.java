package app.estateagency.jpa.repositories;

import app.estateagency.jpa.entities.Agent;
import app.estateagency.jpa.entities.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    Optional<List<Calendar>> findByAgent(Agent agent);
}
