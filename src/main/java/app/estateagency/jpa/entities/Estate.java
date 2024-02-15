package app.estateagency.jpa.entities;

import app.estateagency.enums.estate.Availability;
import app.estateagency.enums.estate.Condition;
import app.estateagency.enums.estate.EstateType;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
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

    @OneToOne(mappedBy = "estate", cascade = CascadeType.ALL)
    private Offer offer;

    @OneToMany(mappedBy = "estate", cascade = CascadeType.ALL)
    private Set<Photo> photos;

    @OneToMany(mappedBy = "estate", cascade = CascadeType.ALL)
    private Set<Document> documents;

    @OneToOne(mappedBy = "estate", cascade = CascadeType.ALL)
    private ArchivedOffer archivedOffer;

    @PrePersist
    private void prePersist() {
        postDate = LocalDateTime.now();
    }
}
