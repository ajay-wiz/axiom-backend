package com.portfolio.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class FileUploadService {

    @Autowired
    private Cloudinary cloudinary;

    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    /**
     * Uploads a file to Cloudinary and returns the secure URL.
     * @param file The multipart file to upload
     * @param folder The folder in Cloudinary to upload to (e.g. "portfolio/thumbnails")
     * @return The secure HTTPS URL of the uploaded file
     */
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        // If Cloudinary is not configured, throw an exception or handle fallback
        if (cloudName == null || cloudName.isEmpty() || cloudName.trim().isEmpty()) {
            throw new IllegalStateException("Cloudinary is not configured. Please set CLOUDINARY_CLOUD_NAME, CLOUDINARY_API_KEY, and CLOUDINARY_API_SECRET.");
        }

        try {
            // Generate a unique filename using UUID
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String publicId = UUID.randomUUID().toString();

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", folder,
                    "public_id", publicId,
                    "resource_type", "auto" // Automatically detect image or video
            ));

            // Return the secure URL provided by Cloudinary
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            throw new IOException("Failed to upload file to Cloudinary", e);
        }
    }
}
