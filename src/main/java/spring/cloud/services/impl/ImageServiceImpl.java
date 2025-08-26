package spring.cloud.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import spring.cloud.dtos.images.ImageResponse;
import spring.cloud.exceptions.InvalidOperationException;
import spring.cloud.exceptions.ResourceNotFoundException;
import spring.cloud.mappers.ImageMapper;
import spring.cloud.repositories.ImageRepository;
import spring.cloud.services.CognitoService;
import spring.cloud.services.ImageService;
import spring.cloud.services.S3Service;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final CognitoService cognitoService;
    private final S3Service s3Service;
    private final ImageMapper imageMapper;
    private final ImageRepository imageRepository;

    @Override
    public Page<ImageResponse> getUserActiveImages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        var user = cognitoService.getCurrentUser();
        var userImagesPage = imageRepository.findByUser_IdAndDeletedFalseOrderByCreatedAtDesc(user.getId(), pageable);

        return userImagesPage.map(imageMapper::toResponse);
    }

    @Override
    public Page<ImageResponse> getRecycleBinImages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        var user = cognitoService.getCurrentUser();
        var recycleBinImagesPage = imageRepository.findByUser_IdAndDeletedTrueOrderByUpdatedAtDesc(user.getId(), pageable);

        return recycleBinImagesPage.map(imageMapper::toResponse);
    }

    @Override
    public void softDeleteImage(Long imageId) {
        var image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Image with ID: %d not found", imageId), HttpStatus.NOT_FOUND));
        var user = cognitoService.getCurrentUser();

        if (!image.getUser().getId().equals(user.getId()))
            throw new InvalidOperationException("Unauthorized to delete this image");

        s3Service.softDeleteImage(image.getUrl());

        image.setDeleted(Boolean.TRUE);
        imageRepository.save(image);
    }

    @Override
    public void restoreImage(Long imageId) {
        var image = imageRepository.findById(imageId)
                .orElseThrow(() -> new InvalidOperationException("Image not found with id: " + imageId));

        var currentUser = cognitoService.getCurrentUser();
        if (!image.getUser().getId().equals(currentUser.getId())) {
            throw new InvalidOperationException("Unauthorized to restore this image");
        }

        if (Boolean.FALSE.equals(image.getDeleted())) {
            throw new InvalidOperationException("Image is not deleted and cannot be restored");
        }

        s3Service.restoreImage(image.getUrl());

        image.setDeleted(Boolean.FALSE);
        imageRepository.save(image);
    }

    @Override
    public void permanentlyDeleteImage(Long imageId) {
        var image = imageRepository.findById(imageId)
                .orElseThrow(() -> new InvalidOperationException("Image not found with id: " + imageId));

        var currentUser = cognitoService.getCurrentUser();
        if (!image.getUser().getId().equals(currentUser.getId()))
            throw new InvalidOperationException("Unauthorized to permanently delete this image");

        s3Service.permanentDeleteImage(image.getUrl());
        imageRepository.delete(image);
    }

    @Override
    public ImageResponse getImageById(Long id) {
        var image = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Image with ID: %d not found", id), HttpStatus.NOT_FOUND));
        var user = cognitoService.getCurrentUser();

        if (!image.getUser().getId().equals(user.getId()))
            throw new InvalidOperationException("Unauthorized to permanently delete this image");

        return imageMapper.toResponse(image);
    }
}
