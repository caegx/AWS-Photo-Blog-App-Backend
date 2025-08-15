package spring.cloud.dtos;

public record WatermarkResponse(
        String waterMarkedUrl,
        boolean success
) {}
