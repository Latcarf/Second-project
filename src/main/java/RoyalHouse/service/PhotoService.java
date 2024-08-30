package RoyalHouse.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class PhotoService {

    @Value("${photo.storage.path}")
    private String photoStoragePath;

    public List<String> saveRealEstatePhotos(List<MultipartFile> files, String type, String name, Long id) {
        String directoryPath = String.format("entity/RealEstate/%s/%s_%d", type, name, id);
        return savePhotos(files, directoryPath);
    }

    public String saveNewBuildingBanner(MultipartFile file, String name, Long id) {
        String directoryPath = String.format("entity/NewBuilding/%s_%d/banner", name, id);
        return savePhotoFile(file, directoryPath);
    }

    public String saveNewBuildingPanorama(MultipartFile file, String name, Long id) {
        String directoryPath = String.format("entity/NewBuilding/%s_%d/panorama", name, id);
        return savePhotoFile(file, directoryPath);
    }

    public List<String> saveNewBuildingPhotos(List<MultipartFile> files, String name, Long id) {
        String directoryPath = String.format("entity/NewBuilding/%s_%d/photos", name, id);
        return savePhotos(files, directoryPath);
    }

    public List<String> saveInfrastructurePhotos(List<MultipartFile> files, String newBuildingName, Long newBuildingId) {
        String directoryPath = String.format("entity/Infrastructure/%s_%d", newBuildingName, newBuildingId);
        return savePhotos(files, directoryPath);
    }

    private String savePhotoFile(MultipartFile file, String directoryPath) {
        try {
            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path path = Paths.get(photoStoragePath, directoryPath);

            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            Path filePath = path.resolve(uniqueFileName);
            Files.write(filePath, file.getBytes());

            return "/" + Paths.get(directoryPath, uniqueFileName).toString().replace("\\", "/");
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    private List<String> savePhotos(List<MultipartFile> files, String directoryPath) {
        List<String> photoUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String photoUrl = savePhotoFile(file, directoryPath);
            photoUrls.add(photoUrl);
        }
        return photoUrls;
    }

    public void deleteSpecificPhotos(List<String> photoUrls) {
        if (Objects.nonNull(photoUrls)) {
            for (String url : photoUrls) {
                try {
                    Path filePath = Paths.get(photoStoragePath, url.replace("/images/", ""));
                    Files.deleteIfExists(filePath);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to delete file: " + url, e);
                }
            }
        }
    }

    public void deletePhotoDirectory(String type, String name, Long id) {
        String directoryPath = Paths.get(photoStoragePath, "entity", type, String.format("%s_%d", name, id)).toString();
        try {
            Path directory = Paths.get(directoryPath);
            if (Files.exists(directory)) {
                Files.walk(directory)
                        .sorted()
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                throw new RuntimeException("Failed to delete: " + path, e);
                            }
                        });
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete directory: " + directoryPath, e);
        }
    }
}
