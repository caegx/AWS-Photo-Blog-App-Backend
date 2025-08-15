package spring.cloud.dtos;

public record WatermarkRequest(
        String text,
        String position,
        String color,
        Integer fontSize
) {}
