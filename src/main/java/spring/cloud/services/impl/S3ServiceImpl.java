package spring.cloud.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import spring.cloud.config.S3Config;
import spring.cloud.dtos.WatermarkRequest;
import spring.cloud.exceptions.InvalidOperationException;
import spring.cloud.services.S3Service;
import spring.cloud.services.WatermarkService;

import java.io.IOException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class S3ServiceImpl implements S3Service {
    private final S3Client s3Client;
    private final S3Config s3Config;
    private final WatermarkService watermarkService;

    @Override
    public String uploadImage(MultipartFile file, WatermarkRequest request) {
        var imageKey = generateImageKey(file);
        var objectUrl = uploadImageToS3(imageKey, file);

        return watermarkService.addWatermark(objectUrl, request);
    }

    private String uploadImageToS3(String key, MultipartFile file) {
        try {
            var putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Config.stagingBucketName())
                    .key(key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return String.format("https://%s.s3.%s.amazonaws.com/%s",
                    s3Config.stagingBucketName(), s3Config.region(), key);

        } catch (IOException e) {
            throw new InvalidOperationException("Failed to read uploaded file: " + e.getMessage());
        } catch (S3Exception e) {
            throw new InvalidOperationException("S3 upload failed: " + e.getMessage());
        }
    }

    private String generateImageKey(MultipartFile file) {
        var fileName = file.getOriginalFilename();
        var extension = "";

        if (fileName != null && fileName.contains("."))
            extension = fileName.substring(fileName.lastIndexOf("."));

        return String.format("images/unprocessed_%d_%s%s",
                System.currentTimeMillis(),
                UUID.randomUUID().toString().substring(0, 8),
                extension);

    }
}