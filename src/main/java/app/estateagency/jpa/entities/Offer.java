package app.estateagency.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "offer", schema = "real_estate")
@Data
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "estate_id")
    private Estate estate;

    private double price;

    private String description;

    private LocalDateTime postDate;

    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL)
    private Set<OfferVisit> offerVisits;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    private Set<Customer> customers;

    private boolean blocked;

    @ManyToOne
    @JoinColumn(name = "blocked_by")
    private Customer blockedBy;

    @PrePersist
    private void prePersist() {
        postDate = LocalDateTime.now();
    }
}
