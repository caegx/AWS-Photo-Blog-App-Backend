package spring.cloud.dtos.images;

public record WatermarkRequest(
        String text,
        String position,
        String color,
        Integer fontSize
) {}
