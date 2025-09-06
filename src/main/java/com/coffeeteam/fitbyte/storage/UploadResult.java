package com.coffeeteam.fitbyte.storage;

public record UploadResult(
        String bucket,
        String objectName,
        String contentType,
        long size,
        String etag,
        String presignedUrl
) {}
