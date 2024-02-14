package app.estateagency.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "document", schema = "real_estate")
@Data
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "estate_id")
    private Estate estate;

    private String filename;

    private String filePath;

    private Long fileSize;

    private String contentType;

    private LocalDateTime uploadDate;

    @PrePersist
    private void prePersist() {
        uploadDate = LocalDateTime.now();
    }
}
