package spring.cloud.dtos.images;

import java.time.LocalDateTime;

public record ImageResponse(
        Long id,
        String url,
        Boolean deleted,
        LocalDateTime createdAt
) {}
