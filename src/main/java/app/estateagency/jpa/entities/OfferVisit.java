package app.estateagency.jpa.entities;

import javax.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "offer_visits", schema = "real_estate")
@Data
public class OfferVisit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "offer_id")
    private Offer offer;

    private LocalDateTime date;

    @PrePersist()
    private void prePersist() {
        date = LocalDateTime.now();
    }
}
