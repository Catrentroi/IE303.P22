package com.example.shoesapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve static images from the BTQT4 directory
        registry.addResourceHandler("/img/**")
               .addResourceLocations("file:./");
        
        // Also serve images from the images directory
        registry.addResourceHandler("/images/**")
               .addResourceLocations("file:./");
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allow cross-origin requests from any origin
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false);
    }
}
