package app.estateagency.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "archived_offer", schema = "real_estate")
@Data
public class ArchivedOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "estate_id")
    private Estate estate;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private double price;

    private String description;

    private LocalDateTime archiveDate;

    @OneToOne(mappedBy = "historyOffer", cascade = CascadeType.ALL)
    private Review review;

    @PrePersist
    private void prePersist() {
        archiveDate = LocalDateTime.now();
    }
}
