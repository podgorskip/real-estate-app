package app.estateagency.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;
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
    private Set<CustomerMeeting> meetings;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<OfferVisit> offerVisits;

    @ManyToMany(mappedBy = "customers")
    private Set<Offer> likes;

    @ManyToMany(mappedBy = "customers")
    private Set<Preference> preferences;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private CreditWorthinessInfo creditWorthinessInfo;

    @OneToMany(mappedBy = "customer")
    private Set<CustomerReview> reviews;
}
