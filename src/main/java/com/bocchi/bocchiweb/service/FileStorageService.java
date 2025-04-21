package com.bocchi.bocchiweb.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;

    public FileStorageService(@Value("${app.file.upload-dir}") String uploadDir) throws IOException {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.fileStorageLocation);
    }

    public String saveFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Empty file");
            }

            String fileName = UUID.randomUUID() + "_" + StringUtils.cleanPath(file.getOriginalFilename());

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/products/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Could not store file. Error: " + e.getMessage(), e);
        }
    }

    public void deleteFile(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank())
            return;

        String fileName = Paths.get(imageUrl).getFileName().toString();
        Path filePath = this.fileStorageLocation.resolve(fileName);

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + fileName, e);
        }
    }
}
