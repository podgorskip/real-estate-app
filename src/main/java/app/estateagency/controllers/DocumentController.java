package app.estateagency.controllers;

import app.estateagency.dto.Mapper;
import app.estateagency.dto.request.DocumentRequest;
import app.estateagency.dto.response.DocumentResponse;
import app.estateagency.dto.response.Response;
import app.estateagency.enums.Privilege;
import app.estateagency.jpa.entities.Document;
import app.estateagency.security.RequiredPrivilege;
import app.estateagency.services.DocumentService;
import jakarta.validation.Valid;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DocumentController {
    private final DocumentService documentService;

    @RequiredPrivilege(Privilege.ADD_DOCUMENTS)
    @PostMapping("/owner/upload-document")
    public ResponseEntity<Response> addDocuments(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("file") MultipartFile file, @RequestParam("id") Long id) {
        Response response = documentService.addDocument(file, id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

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
