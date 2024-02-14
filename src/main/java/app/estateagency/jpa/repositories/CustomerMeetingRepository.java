package app.estateagency.jpa.repositories;

import app.estateagency.jpa.entities.CustomerMeeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerMeetingRepository extends JpaRepository<CustomerMeeting, Long> {
}
