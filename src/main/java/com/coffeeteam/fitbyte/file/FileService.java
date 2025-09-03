package com.coffeeteam.fitbyte.file;

import com.coffeeteam.fitbyte.storage.ObjectStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final ObjectStorageService storage;

    public FileResponse upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is required");
        }
        if (file.getSize() > 100 * 1024) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File size exceeds 100 KB");
        }
        String contentType = file.getContentType();
        List<String> allowedTypes = List.of("image/jpeg", "image/jpg", "image/png");
        if (contentType == null || !allowedTypes.contains(contentType)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid file type, only jpeg/jpg/png allowed"
            );
        }

        var result = storage.upload(file, "uploads");
        return new FileResponse(result.presignedUrl());
    }
}
