package com.instagram.config;

import com.instagram.converter.MultipartListToFileListConverter;
import com.instagram.converter.MultipartToFileListConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MainConfig implements WebMvcConfigurer {
    @Autowired
    private MultipartListToFileListConverter multipartToFileListConverter;
    @Autowired
    private MultipartToFileListConverter multipartToFileConverter;
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(multipartToFileConverter);
        registry.addConverter(multipartToFileListConverter);
    }
}
