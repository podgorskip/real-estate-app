package app.estateagency.jpa.entities;

import app.estateagency.enums.Role;
import javax.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "review", schema = "real_estate")
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToOne
    @JoinColumn(name = "offer_id")
    private ArchivedOffer archivedOffer;

    private int rating;

    private String comment;

    private LocalDateTime postDate;

    @PrePersist
    private void prePersist() {
        postDate = LocalDateTime.now();
    }
}
