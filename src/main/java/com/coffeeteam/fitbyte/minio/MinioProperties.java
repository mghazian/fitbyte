package com.coffeeteam.fitbyte.minio;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio")
public record MinioProperties(
        String endpoint,
        String accessKey,
        String secretKey,
        String bucket,
        String region,
        boolean secure,
        int presignExpirySeconds,
        String publicEndpoint
) {}
