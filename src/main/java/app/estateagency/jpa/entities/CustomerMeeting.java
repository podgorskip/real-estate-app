package app.estateagency.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Entity
@Table(name = "customer_meeting", schema = "real_estate")
@Data
public class CustomerMeeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "offer_id")
    private Offer offer;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    private LocalDateTime date;
}
