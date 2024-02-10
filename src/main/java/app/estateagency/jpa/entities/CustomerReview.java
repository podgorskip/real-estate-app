package app.estateagency.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_review", schema = "real_estate")
@Data
public class CustomerReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "offer_id")
    private HistoryOffer historyOffer;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private int rating;

    private String comment;

    private LocalDateTime postDate;

    @PrePersist
    private void prePersist() {
        postDate = LocalDateTime.now();
    }
}
