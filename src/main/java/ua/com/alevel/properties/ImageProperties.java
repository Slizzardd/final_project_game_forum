package ua.com.alevel.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "image")
public class ImageProperties {

    private String maxSizeImage;

    public String getMaxSizeImage() {
        return maxSizeImage;
    }

    public void setMaxSizeImage(String maxSizeImage) {
        this.maxSizeImage = maxSizeImage;
    }
}
