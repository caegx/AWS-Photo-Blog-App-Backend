package spring.cloud.services;

import spring.cloud.dtos.WatermarkRequest;

public interface WatermarkService {
    String addWatermark(String s3ImageUrl, WatermarkRequest request);
}
