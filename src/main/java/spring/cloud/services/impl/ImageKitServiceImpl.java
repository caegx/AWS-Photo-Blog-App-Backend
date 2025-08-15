package spring.cloud.services.impl;

import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.cloud.config.ImagekitConfig;
import spring.cloud.dtos.WatermarkRequest;
import spring.cloud.exceptions.InvalidOperationException;
import spring.cloud.services.WatermarkService;

@Service
@AllArgsConstructor
public class ImageKitServiceImpl implements WatermarkService {
    private final ImageKit imageKit;
    private final ImagekitConfig imagekitConfig;

    @Override
    public String addWatermark(String s3ImageUrl, WatermarkRequest request) {

        var imagePath = uploadS3ImageToImagekit(s3ImageUrl);

        String textTransform = "l-text" +
                ",i-" + encodeText(request.text()) +
                ",co-" + request.color() +
                ",fs-" + request.fontSize() +
                ",ff-Arial" +
                ",pa-15" +
                getPositionTransform(request.position()) +
                ",l-end";

        return imagekitConfig.urlEndpoint() +
                "/tr:" + textTransform +
                imagePath;
    }

    private String uploadS3ImageToImagekit(String s3ImageUrl)  {
        String fileName = "watermark_" + System.currentTimeMillis() + ".png";

        FileCreateRequest fileCreateRequest = new FileCreateRequest(s3ImageUrl, fileName);
        fileCreateRequest.setUseUniqueFileName(true);
        fileCreateRequest.setFolder("/watermarked/");

        Result uploadResult;
        try {
            uploadResult = imageKit.upload(fileCreateRequest);
        } catch (Exception e) {
            throw new InvalidOperationException(e.getMessage());
        }

        if (uploadResult == null || uploadResult.getFilePath() == null) {
            throw new InvalidOperationException("Failed to upload image to ImageKit");
        }

        return uploadResult.getFilePath();
    }

    private String encodeText(String text) {
        return text.replace(" ", "%20")
                .replace("©", "%C2%A9")
                .replace("®", "%C2%AE")
                .replace("&", "%26")
                .replace("#", "%23");
    }

    private String getPositionTransform(String position) {
        return switch (position.toLowerCase()) {
            case "bottom-left" -> ",lfo-bottom_left";
            case "top-right" -> ",lfo-top_right";
            case "top-left" -> ",lfo-top_left";
            case "center" -> ",ia-center";
            case "bottom-center" -> ",lfo-bottom";
            case "top-center" -> ",lfo-top";
            default -> ",lfo-bottom_right";
        };
    }
}
