package app.estateagency.jpa.entities;

import javax.persistence.*;
import lombok.Data;
import java.util.Set;

@Entity
@Table(name = "preference", schema = "real_estate")
@Data
public class Preference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "preference_customer",
            schema = "real_estate",
            joinColumns = @JoinColumn(name = "preference_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    private Set<Customer> customers;
}
