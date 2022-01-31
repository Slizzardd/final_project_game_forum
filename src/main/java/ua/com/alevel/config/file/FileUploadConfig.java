package ua.com.alevel.config.file;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import ua.com.alevel.properties.ImageProperties;

import javax.servlet.MultipartConfigElement;

@Configuration
@ComponentScan
public class FileUploadConfig {

    private ImageProperties imageProperties;

    public FileUploadConfig(ImageProperties imageProperties) {
        this.imageProperties = imageProperties;
    }

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.parse(imageProperties.getMaxSizeImage()));
        factory.setMaxRequestSize(DataSize.parse(imageProperties.getMaxSizeImage()));
        return factory.createMultipartConfig();
    }
}