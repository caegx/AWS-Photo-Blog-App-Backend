package spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import spring.cloud.config.S3Config;
import spring.cloud.config.ImagekitConfig;


@SpringBootApplication
@EnableConfigurationProperties({S3Config.class, ImagekitConfig.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}