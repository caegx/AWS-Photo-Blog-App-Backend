package spring.cloud.services;

import org.springframework.data.domain.Page;
import spring.cloud.dtos.images.ImageResponse;

public interface ImageService {

    Page<ImageResponse> getUserActiveImages(int page, int size);

    Page<ImageResponse> getRecycleBinImages(int page, int size);

    void softDeleteImage(Long imageId);

    void restoreImage(Long imageId);

    void permanentlyDeleteImage(Long imageId);

    ImageResponse getImageById(Long id);
}
