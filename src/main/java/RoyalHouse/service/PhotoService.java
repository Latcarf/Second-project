package RoyalHouse.service;

import RoyalHouse.model.Photo;
import RoyalHouse.model.modelEnum.EntityType;
import RoyalHouse.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

    @Value("${photo.storage.path}")
    private String photoStoragePath;

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public String savePhotoFile(MultipartFile file, EntityType entityType, Long entityId) {
        try {
            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            String entityFolderName = entityType.toString().toLowerCase();
            String entitySpecificFolderName = entityId.toString();
            String subFolderPath = Paths.get(entityFolderName, entitySpecificFolderName).toString();

            Path directoryPath = Paths.get(photoStoragePath, subFolderPath);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            Path filePath = directoryPath.resolve(uniqueFileName);

            Files.write(filePath, file.getBytes());

            return "/" + Paths.get("images", subFolderPath, uniqueFileName).toString().replace("\\", "/");

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    public List<Photo> savePhotos(List<MultipartFile> files, EntityType entityType, Long entityId) {
        List<Photo> photoList = new ArrayList<>();
        for (MultipartFile file : files) {
            String photoUrl = savePhotoFile(file, entityType, entityId);
            Photo photo = new Photo();
            photo.setUrl(photoUrl);
            photo.setEntityId(entityId);
            photo.setEntityType(entityType);
            photoRepository.save(photo);
            photoList.add(photo);
        }
        return photoList;
    }
}
