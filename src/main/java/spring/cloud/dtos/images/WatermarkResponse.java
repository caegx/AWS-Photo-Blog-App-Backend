package spring.cloud.dtos.images;

public record WatermarkResponse(
        String waterMarkedUrl,
        boolean success
) {}
