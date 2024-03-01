package app.estateagency.services;

import app.estateagency.dto.response.Response;
import app.estateagency.jpa.entities.Estate;
import app.estateagency.jpa.entities.Photo;
import app.estateagency.jpa.repositories.PhotoRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.List;
import java.util.Optional;

/**
 *  A service allowing to handle business logic related to Photo entities
 */
@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final EstateService estateService;
    private final ZipperService zipperService;

    @Value("${app.photos-path}")
    private String path;

    /**
     * Allows owners to upload a photo of their estate
     * @param file Photo data
     * @param id ID of the estate to which photos are attached
     * @return Response if successfully uploaded photos
     */
    @Transactional
    public Response uploadPhoto(MultipartFile file, Long id) {
        Optional<Estate> estate = estateService.getByID(id);

        if (estate.isEmpty())
            return new Response(false, HttpStatus.NOT_FOUND, "No estate of the provided ID found");

        Optional<Photo> photo = createPhoto(file, estate.get());

        if (photo.isEmpty())
            return new Response(false, HttpStatus.BAD_REQUEST, "Failed to upload the file");

        photoRepository.save(photo.get());

        return new Response(true, HttpStatus.CREATED, "Successfully uploaded the photo");
    }

    /**
     * Allows to retrieve photos of an estate in ZIP file
     * @param id ID of the estate to which photos are attached
     * @return Byte Stream of the zipped photos if present, empty otherwise
     */
    public Optional<ByteArrayOutputStream> getPhotosByEstateID(Long id) {
        Optional<Estate> estate = estateService.getByID(id);

        if (estate.isEmpty())
            return Optional.empty();

        Optional<List<Photo>> photos = photoRepository.findByEstate(estate.get());

        if (photos.isEmpty())
            return Optional.empty();

        try { return Optional.of(zipperService.zipFolder(photos.get().get(0).getFilePath())); }
        catch (IOException e) { return Optional.empty(); }
    }

    /**
     * Allows to create a fully populated Photo object and save it
     * @param file Photo data
     * @param estate Estate to which photo is attached
     * @return Fully populated Photo object if created successfully, empty otherwise
     */
    private Optional<Photo> createPhoto(MultipartFile file, Estate estate) {

        File directory = new File(path + estate.getId());

        if (!directory.exists())
            directory.mkdirs();

        String filePath = path + estate.getId() + "/";

        try { file.transferTo(new File(filePath + file.getOriginalFilename())); }
        catch (IOException e) { return Optional.empty(); }

        Photo photo = new Photo();

        photo.setFilename(file.getOriginalFilename());
        photo.setFileSize(file.getSize());
        photo.setFilePath(filePath);
        photo.setEstate(estate);
        photo.setContentType(file.getContentType());

        return Optional.of(photo);
    }

}
