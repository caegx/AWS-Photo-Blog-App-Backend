package spring.cloud.services;

import org.springframework.web.multipart.MultipartFile;
import spring.cloud.dtos.WatermarkRequest;

public interface S3Service {
    String uploadImage(MultipartFile file, WatermarkRequest request);
}
