package com.coffeeteam.fitbyte.file;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponse> uploadFile(@RequestPart("file")MultipartFile file){
        var response = fileService.upload(file);
        return ResponseEntity.ok(response);
    }

}
