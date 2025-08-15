package spring.cloud.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "imagekit")
public record ImagekitConfig(
    String urlEndpoint,
    String privateKey,
    String publicKey
) {}
