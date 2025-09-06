package com.coffeeteam.fitbyte.file;

import com.coffeeteam.fitbyte.storage.ObjectStorageService;
import com.coffeeteam.fitbyte.core.service.UserService;
import com.coffeeteam.fitbyte.core.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final ObjectStorageService storage;
    private final UserService userService;

    @Value("${file.upload.max-size-kb:100}")
    private long maxFileSizeKb;

    @Value("${file.upload.allowed-types:image/jpeg,image/png}")
    private Set<String> allowedContentTypes;

    @Value("${file.upload.path:uploads}")
    private String uploadPath;

    private static final Set<String> SUPPORTED_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png");
    private static final String OCTET_STREAM = "application/octet-stream";

    public FileResponse upload(MultipartFile file, Long userId) {
        validateFile(file);
        String contentType = determineContentType(file);
        validateContentType(contentType);

        try {
            User user = userService.getByIdOrThrow(userId);
            String oldImageUri = user.getImageUri();

            if (oldImageUri != null && !oldImageUri.trim().isEmpty()) {
                try {
                    storage.delete(oldImageUri);
                } catch (Exception e) {
                    log.error("Error delete image: {}", oldImageUri);
                }
            }

            var result = storage.upload(file, uploadPath);
            userService.updateImageKeyById(userId, result.objectName());

            return new FileResponse(result.presignedUrl());
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "File upload failed"
            );
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "File is required"
            );
        }

        if (file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "File cannot be empty"
            );
        }

        long maxSizeBytes = maxFileSizeKb * 1024;
        if (file.getSize() > maxSizeBytes) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("File size exceeds maximum allowed size of %d KB", maxFileSizeKb)
            );
        }

        String filename = file.getOriginalFilename();
        if (!StringUtils.hasText(filename)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "File must have a valid name"
            );
        }
    }

    private String determineContentType(MultipartFile file) {
        String contentType = file.getContentType();

        // If content type is missing or generic, try to determine from filename
        if (!StringUtils.hasText(contentType) || OCTET_STREAM.equals(contentType)) {
            contentType = getContentTypeFromFilename(file.getOriginalFilename());
        }

        return contentType;
    }

    private String getContentTypeFromFilename(String filename) {
        if (!StringUtils.hasText(filename)) {
            return null;
        }

        String lowerFilename = filename.toLowerCase();

        if (lowerFilename.endsWith(".jpg") || lowerFilename.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (lowerFilename.endsWith(".png")) {
            return "image/png";
        }

        return null;
    }

    private void validateContentType(String contentType) {
        if (!StringUtils.hasText(contentType) || !allowedContentTypes.contains(contentType)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Invalid file type. Allowed types: %s",
                            String.join(", ", allowedContentTypes))
            );
        }
    }
}