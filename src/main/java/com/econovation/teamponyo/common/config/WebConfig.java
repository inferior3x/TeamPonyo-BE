package com.econovation.teamponyo.common.config;

import com.econovation.teamponyo.common.enums.StringToExhibitCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final StringToExhibitCategory stringToExhibitCategory;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToExhibitCategory);
    }
}
