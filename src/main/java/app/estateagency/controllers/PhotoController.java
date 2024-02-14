package app.estateagency.controllers;

import app.estateagency.dto.response.Response;
import app.estateagency.enums.Privilege;
import app.estateagency.security.RequiredPrivilege;
import app.estateagency.services.PhotoService;
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Optional;

/**
 *  A controller handling requests regarding Photo entities
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PhotoController {
    private final PhotoService photoService;

    /**
     * Allows owners to upload a photo of their estate
     * @param userDetails User details used to validate required privilege
     * @param file Photo data
     * @param id ID of the estate of which photos are uploaded
     * @return Response if successfully uploaded the photo
     */
    @RequiredPrivilege(Privilege.UPLOAD_PHOTO)
    @PostMapping("/owner/upload-photo")
    public ResponseEntity<Response> uploadPhoto(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("file") MultipartFile file, @RequestParam("id") Long id) {
        Response response = photoService.uploadPhoto(file, id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Allows users to retrieve photos of the estate in ZIP format
     * @param id ID of the estate of which photos are retrieved
     * @return Response with ZIP format file containing photos
     */
    @GetMapping(value = "/auth/photos",  produces="application/zip")
    public ResponseEntity<InputStreamResource> getPhotos(@RequestParam("id") Long id) {
        Optional<ByteArrayOutputStream> optionalPhotos = photoService.getPhotosByEstateID(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "estate:" + id + ".zip");

        return optionalPhotos
                .map(photos -> ResponseEntity
                        .status(HttpStatus.OK)
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(new InputStreamResource(new ByteArrayInputStream(optionalPhotos.get().toByteArray()))))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }
}
