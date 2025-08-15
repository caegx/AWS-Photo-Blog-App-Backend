package spring.cloud.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import spring.cloud.dtos.WatermarkRequest;
import spring.cloud.dtos.WatermarkResponse;
import spring.cloud.services.S3Service;

@RestController
@RequestMapping("/api/watermark")
@AllArgsConstructor
public class WatermarkController {

    private final S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<WatermarkResponse> addWatermark(
            @RequestParam("file") MultipartFile file,
            @RequestParam("text") String text,
            @RequestParam("position") String position,
            @RequestParam("color") String color,
            @RequestParam("fontSize") Integer fontSize
            ) {

        var request = new WatermarkRequest(text, position, color, fontSize);

        var imageUrl = s3Service.uploadImage(file, request);
        return ResponseEntity.ok(new WatermarkResponse(imageUrl, true));
    }
}
