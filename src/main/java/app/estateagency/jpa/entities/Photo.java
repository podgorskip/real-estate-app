package app.estateagency.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "photo", schema = "real_estate")
@Data
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "estate_id")
    private Estate estate;

    private String filename;

    private String contentType;

    private int fileSize;

    private LocalDateTime uploadDate;

    @PrePersist
    private void prePersist() {
        uploadDate = LocalDateTime.now();
    }
}
