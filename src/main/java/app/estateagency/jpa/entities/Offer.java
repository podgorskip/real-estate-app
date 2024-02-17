package app.estateagency.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Objects;
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

    @OneToMany(mappedBy = "offer")
    private Set<OfferVisit> offerVisits;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "likes",
            schema = "real_estate",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    private Set<Customer> customers;

    private boolean blocked;

    @ManyToOne
    @JoinColumn(name = "blocked_by")
    private Customer blockedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return Objects.equals(id, offer.id) && Objects.equals(estate, offer.estate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, estate);
    }

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", estate=" + estate +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", postDate=" + postDate +
                '}';
    }

    @PrePersist
    private void prePersist() {
        postDate = LocalDateTime.now();
    }
}
