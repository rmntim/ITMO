package ru.rmntim.web.service;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Stateless
@Slf4j
public class AvatarService {
    @Resource(lookup = "java:global/s3/endpoint")
    private String ENDPOINT_URL;
    @Resource(lookup = "java:global/s3/accessKey")
    private String ACCESS_KEY;
    @Resource(lookup = "java:global/s3/secretKey")
    private String SECRET_KEY;
    @Resource(lookup = "java:global/s3/bucketName")
    private String BUCKET_NAME;

    private S3Client getS3Client() {
        return S3Client.builder()
                .region(Region.of("ru-central1"))
                .endpointOverride(java.net.URI.create(ENDPOINT_URL))
                .credentialsProvider(
                        StaticCredentialsProvider.create(AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY))
                )
                .build();
    }

    public String uploadAvatar(InputStream fileInputStream, MediaType mediaType) {
        try {
            var fileName = UUID.randomUUID() + "." + mediaType.getSubtype();
            var s3Client = getS3Client();

            var byteArrayOutputStream = new ByteArrayOutputStream();
            var buffer = new byte[2 * 1024 * 1024]; // TAK POXUI
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            var fileData = byteArrayOutputStream.toByteArray();

            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(BUCKET_NAME)
                            .key(fileName)
                            .contentType(mediaType.toString())
                            .build(),
                    RequestBody.fromBytes(fileData)
            );

            return ENDPOINT_URL + "/" + BUCKET_NAME + "/" + fileName;
        } catch (IOException e) {
            log.error("Error uploading avatar {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
