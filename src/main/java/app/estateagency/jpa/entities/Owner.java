package app.estateagency.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "owner", schema = "real_estate")
@Data
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private Set<Estate> estates;

    @Override
    public int hashCode() {
        return Objects.hash(id, user.getUsername());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Owner otherOwner = (Owner) o;

        return id.equals(otherOwner.getId()) && user.equals(otherOwner.getUser());
    }
}
