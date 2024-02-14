package app.estateagency.jpa.repositories;

import app.estateagency.jpa.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<Document> findByFilename(String filename);

    @Query("SELECT d FROM Document d " +
            "JOIN Estate e ON e = d.estate " +
            "WHERE e.id = :id")
    Optional<Document> findByEstateID(@Param("id") Long id);
}
