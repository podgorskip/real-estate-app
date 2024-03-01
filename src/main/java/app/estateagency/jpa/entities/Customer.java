package app.estateagency.jpa.entities;

import javax.persistence.*;
import lombok.Data;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "customer", schema = "real_estate")
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<OfferVisit> offerVisits;

    @ManyToMany(mappedBy = "customers")
    private Set<Offer> likes;

    @ManyToMany(mappedBy = "customers")
    private Set<Preference> preferences;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private CreditworthinessInfo creditWorthinessInfo;

    @OneToMany(mappedBy = "customer")
    private Set<ArchivedOffer> archivedOffers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) && Objects.equals(user, customer.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", user=" + user +
                '}';
    }
}
