package com.jinwoo.snsbackend_mainserver.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    private final long MAX_AGE_SECS = 3600;

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry){
        corsRegistry
                .addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET","POST","PATCH","DELETE","PUT","OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(MAX_AGE_SECS);

    }

}
