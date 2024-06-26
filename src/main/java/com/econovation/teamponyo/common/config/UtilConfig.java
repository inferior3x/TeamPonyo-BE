package com.econovation.teamponyo.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;

@Configuration
public class UtilConfig {
    @Bean
    public UriBuilderFactory uriBuilderFactory(){
        return new DefaultUriBuilderFactory();
    }
}