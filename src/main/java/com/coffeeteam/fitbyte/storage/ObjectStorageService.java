package com.coffeeteam.fitbyte.storage;

import com.coffeeteam.fitbyte.minio.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class ObjectStorageService {

    private final MinioClient minio;
    private final MinioProperties props;

    public ObjectStorageService(MinioClient minio, MinioProperties props) {
        this.minio = minio;
        this.props = props;
    }

    private void ensureBucket() {
        try {
            boolean exists = minio.bucketExists(
                    BucketExistsArgs.builder().bucket(props.bucket()).build()
            );
            if (!exists) {
                minio.makeBucket(MakeBucketArgs.builder().bucket(props.bucket()).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to ensure bucket: " + e.getMessage(), e);
        }
    }

    public UploadResult upload(MultipartFile file, String folder) {
        ensureBucket();
        try {
            String original = file.getOriginalFilename();
            String ext = (original != null && original.contains(".")) ?
                    original.substring(original.lastIndexOf('.')) : "";
            String keyPrefix = (folder != null && !folder.isBlank()) ? folder.strip() + "/" : "";
            String objectName = keyPrefix + UUID.randomUUID() + ext;

            String contentType = file.getContentType();
            if (contentType == null || contentType.isBlank()) {
                contentType = "application/octet-stream";
            }

            try (InputStream is = file.getInputStream()) {
                PutObjectArgs args = PutObjectArgs.builder()
                        .bucket(props.bucket())
                        .object(objectName)
                        .contentType(contentType)
                        .stream(is, file.getSize(), -1)
                        .build();

                ObjectWriteResponse resp = minio.putObject(args);

                String presigned = presignGet(objectName);

                return new UploadResult(
                        props.bucket(),
                        objectName,
                        contentType,
                        file.getSize(),
                        resp.etag(),
                        presigned
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed Upload: " + e.getMessage(), e);
        }
    }

    public String presignGet(String objectName) {
        try {
            String presignEndpoint = props.publicEndpoint();

            MinioClient presignClient = MinioClient.builder()
                    .endpoint(presignEndpoint)
                    .credentials(props.accessKey(), props.secretKey())
                    .region(props.region())
                    .build();

            return presignClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(props.bucket())
                            .object(objectName)
                            .expiry(props.presignExpirySeconds())
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to presigned URL: " + e.getMessage(), e);
        }
    }
}
