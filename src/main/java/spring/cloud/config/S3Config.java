package spring.cloud.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws.s3")
public record S3Config(
        String stagingBucketName,
        String processedBucketName,
        String region
) {}
