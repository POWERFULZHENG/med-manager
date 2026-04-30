package com.xzzj.medmanager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class FileUploadConfig implements WebMvcConfigurer {

    public static final String UPLOAD_PATH = System.getProperty("user.dir") + File.separator + "upload" + File.separator;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + UPLOAD_PATH);
    }
}
