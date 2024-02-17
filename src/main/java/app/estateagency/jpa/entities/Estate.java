package app.estateagency.jpa.entities;

import app.estateagency.enums.estate.Availability;
import app.estateagency.enums.estate.Condition;
import app.estateagency.enums.estate.EstateType;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "estate", schema = "real_estate")
@Data
public class Estate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @Enumerated(value = EnumType.STRING)
    private EstateType type;

    private int bathrooms;

    private int rooms;

    private boolean garage;

    private int storey;

    private String location;

    private boolean balcony;

    private String description;

    @Enumerated(value = EnumType.STRING)
    private Availability availability;

    private double size;

    @Enumerated(value = EnumType.STRING)
    private Condition condition;

    private double offeredPrice;

    private LocalDateTime postDate;

    @OneToOne(mappedBy = "estate")
    private Offer offer;

    @OneToMany(mappedBy = "estate")
    private Set<Photo> photos;

    @OneToMany(mappedBy = "estate")
    private Set<Document> documents;

    @OneToOne(mappedBy = "estate")
    private ArchivedOffer archivedOffer;

    @Override
    public int hashCode() {
        return Objects.hash(id, owner.getUser().getUsername());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Estate otherEstate = (Estate) o;

        return otherEstate.getId().equals(id) && otherEstate.getOwner().getUser().equals(owner.getUser());
    }

    @Override
    public String toString() {
        return "Estate{" +
                "id=" + id +
                ", owner=" + owner +
                ", agent=" + agent +
                ", type=" + type +
                ", bathrooms=" + bathrooms +
                ", rooms=" + rooms +
                ", garage=" + garage +
                ", storey=" + storey +
                ", location='" + location + '\'' +
                ", balcony=" + balcony +
                ", description='" + description + '\'' +
                ", availability=" + availability +
                ", size=" + size +
                ", condition=" + condition +
                ", offeredPrice=" + offeredPrice +
                ", postDate=" + postDate +
                '}';
    }

    @PrePersist
    private void prePersist() {
        postDate = LocalDateTime.now();
    }
}
