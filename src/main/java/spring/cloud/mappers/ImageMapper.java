package spring.cloud.mappers;

import org.mapstruct.Mapper;
import spring.cloud.dtos.images.ImageResponse;
import spring.cloud.entities.Image;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageResponse toResponse(Image image);
}
