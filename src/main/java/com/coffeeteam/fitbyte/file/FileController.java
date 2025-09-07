package com.coffeeteam.fitbyte.file;

import com.coffeeteam.fitbyte.auth.security.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping
    public ResponseEntity<FileResponse> upload(
            @RequestPart("file") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetail userDetail
            ) {
        return ResponseEntity.ok(fileService.upload(file, userDetail.user().getId()));
    }
}
