package app.estateagency.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "offer_history", schema = "real_estate")
@Data
public class HistoryOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "estate_id")
    private Estate estate;

    private double price;

    private String description;

    private LocalDateTime archiveDate;

    @OneToOne(mappedBy = "historyOffer", cascade = CascadeType.ALL)
    private CustomerReview customerReview;

    @PrePersist
    private void prePersist() {
        archiveDate = LocalDateTime.now();
    }
}
