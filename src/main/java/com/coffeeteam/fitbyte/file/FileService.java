package com.coffeeteam.fitbyte.file;

import com.coffeeteam.fitbyte.storage.ObjectStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileService {

    private final ObjectStorageService storage;

    public FileResponse upload(MultipartFile file) {
        if(file.isEmpty()){
            throw new IllegalArgumentException("File is required");
        }
        if(file.getSize() > 100 * 1024){
            throw new IllegalArgumentException("File size exceeds 100 KB");
        }
        String contentType = file.getContentType();
        if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
            throw new IllegalArgumentException("Invalid file type, only jpeg/png allowed");
        }

        var result = storage.upload(file, "uploads");
        return new FileResponse(result.presignedUrl());
    }
}
