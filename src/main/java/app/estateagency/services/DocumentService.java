package app.estateagency.services;

import app.estateagency.dto.response.Response;
import app.estateagency.jpa.entities.Document;
import app.estateagency.jpa.entities.Estate;
import app.estateagency.jpa.repositories.DocumentRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * A service allowing to handle business logic related to Document entities
 */
@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final EstateService estateService;

    @Value("${app.documents-path}")
    private String path;

    /**
     * Allows owners to upload a document confirming their ownership of the estate
     * @param file Document data
     * @param id ID of the estate
     * @return Response if successfully uploaded the document
     */
    @Transactional
    public Response addDocument(MultipartFile file, Long id) {

        Optional<Estate> estate = estateService.getByID(id);

        if (estate.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No estate of the provided ID found");

        Optional<Document> document = createDocument(estate.get(), file);

        if (document.isEmpty())
            return new Response(false, HttpStatus.BAD_REQUEST, "Failed to upload the document");

        documentRepository.save(document.get());

        return new Response(true, HttpStatus.CREATED, "Successfully added the document");
    }

    /**
     * Retrieves the document info
     * @param id ID of the estate to which the document is attached
     * @return Document object if present, empty otherwise
     */
    public Optional<Document> getDocumentInfo(Long id) {
        return documentRepository.findByEstateID(id);
    }

    /**
     * Retrieves the document data
     * @param id ID of the estate to which the document is attached
     * @return Stream containing Document data if present, empty otherwise
     */
    public Optional<InputStream> getDocumentData(Long id) {
        Optional<Document> document = documentRepository.findByEstateID(id);

        if (document.isEmpty())
            return Optional.empty();

        try { return Optional.of(new FileInputStream(document.get().getFilePath() + document.get().getFilename())); }
        catch (IOException e) { return Optional.empty(); }
    }

    /**
     * Allows to create a fully populated Document object
     * @param estate Estate to which the document is attached
     * @param file Document data
     * @return Document if successfully created, empty otherwise
     */
    private Optional<Document> createDocument(Estate estate, MultipartFile file) {
        File directory = new File(path + estate.getId());

        if (!directory.exists())
            directory.mkdirs();

        String filePath = path + estate.getId() + "/";

        try { file.transferTo(new File(filePath + file.getOriginalFilename())); }
        catch (IOException e) { return Optional.empty(); }

        Document document = new Document();

        document.setFilePath(filePath);
        document.setFileSize(file.getSize());
        document.setContentType(file.getContentType());
        document.setEstate(estate);
        document.setFilename(file.getOriginalFilename());

        return Optional.of(document);
    }
}
