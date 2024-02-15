package app.estateagency.controllers;

import app.estateagency.dto.Mapper;
import app.estateagency.dto.response.DocumentResponse;
import app.estateagency.dto.response.Response;
import app.estateagency.enums.Privilege;
import app.estateagency.jpa.entities.Document;
import app.estateagency.security.RequiredPrivilege;
import app.estateagency.services.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.Optional;

/**
 *  A controller handling requests regarding Document entities
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DocumentController {
    private final DocumentService documentService;

    /**
     * Allows owners to upload documents needed to validate their ownership of an estate
     * @param userDetails Details of the user sending the request, used to validate required privilege
     * @param file Document data
     * @param id ID of the estate to which the document should be attached
     * @return Response if successfully attached the document
     */
    @RequiredPrivilege(Privilege.ADD_DOCUMENTS)
    @PostMapping("/owner/upload-document")
    public ResponseEntity<Response> addDocuments(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("file") MultipartFile file, @RequestParam("id") Long id) {
        Response response = documentService.addDocument(file, id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Allows agents to check documents attached to an estate
     * @param userDetails Details of the user sending the request, used to validate required privilege
     * @param id ID of the estate to which the document is attached
     * @return Response containing the document data if present
     */
    @RequiredPrivilege(Privilege.CHECK_DOCUMENTS)
    @GetMapping("/agent/documents")
    public ResponseEntity<InputStreamResource> checkDocument(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("id") Long id) {
        Optional<InputStream> documentData = documentService.getDocumentData(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "file");

        return documentData
                .map(inputStream -> ResponseEntity
                        .status(HttpStatus.OK)
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(new InputStreamResource(inputStream)))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }

    /**
     * Allows agents to retrieve info about the document attached to an estate
     * @param userDetails Details of the user sending the request, used to validate required privilege
     * @param id ID of the estate to which the document is attached
     * @return Response containing the document info if present
     */
    @RequiredPrivilege(Privilege.CHECK_DOCUMENTS)
    @GetMapping("/agent/documents-info")
    public ResponseEntity<DocumentResponse> checkDocumentInfo(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("id") Long id) {
        Optional<Document> optionalDocument = documentService.getDocumentInfo(id);

        return optionalDocument
                .map(document -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(Mapper.INSTANCE.convertDocument(document)))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }
}
