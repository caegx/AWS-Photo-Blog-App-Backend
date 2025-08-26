package spring.cloud.services;

import spring.cloud.dtos.images.WatermarkRequest;

public interface ImageKitService {
    String addWatermark(String s3ImageUrl, WatermarkRequest request);
}
