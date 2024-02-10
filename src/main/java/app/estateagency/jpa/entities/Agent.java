package app.estateagency.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Entity
@Table(name = "agent", schema = "real_estate")
@Data
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "agent")
    private Set<Calendar> calendars;

    @OneToMany(mappedBy = "agent")
    private Set<Estate> estates;

    @OneToMany(mappedBy = "agent")
    private Set<OwnerMeeting> ownerMeetings;

    @OneToMany(mappedBy = "agent")
    private Set<CustomerMeeting> customerMeetings;
}
