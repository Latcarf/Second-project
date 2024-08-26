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

    public String savePhotoFile(MultipartFile file, String type, String name, Long id) {
        try {
            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            String directoryName = name + "_id-" + id;
            Path directoryPath = Paths.get(photoStoragePath, type, directoryName);

            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Path filePath = directoryPath.resolve(uniqueFileName);

            Files.write(filePath, file.getBytes());

            return "/" + Paths.get("images", type, directoryName, uniqueFileName)
                    .toString().replace("\\", "/");

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    public List<String> savePhotos(List<MultipartFile> files, String type, String name, Long id) {
        List<String> photoUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String photoUrl = savePhotoFile(file, type, name, id);
            photoUrls.add(photoUrl);
        }
        return photoUrls;
    }

    public void deletePhotoFile(List<String> photosUrls) {
        if (Objects.nonNull(photosUrls)) {
            for (String url : photosUrls) {
                try {
                    Path filePath = Paths.get(photoStoragePath, url.replace("/images/", ""));
                    Files.deleteIfExists(filePath);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to delete file: " + url, e);
                }
            }
        }
    }

}
