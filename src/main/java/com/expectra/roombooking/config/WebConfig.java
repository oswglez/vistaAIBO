package com.expectra.roombooking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                .defaultContentType(MediaType.APPLICATION_JSON)
                .ignoreAcceptHeader(true);  // Ignora el encabezado Accept
    }

    public WebConfig() {
    }
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/api/**") // Check this pattern
//                .allowedOrigins("http://localhost:5173") // Make sure your FE port is here
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // Is POST included here?
//                .allowedHeaders("*")
//                .allowCredentials(true)
//                .maxAge(3600);
//    }
}