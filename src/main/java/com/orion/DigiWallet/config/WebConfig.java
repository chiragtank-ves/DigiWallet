package com.orion.DigiWallet.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS Configuration for DigiWallet Application
 *
 * This configuration allows the frontend (running on port 3000)
 * to communicate with the backend API (running on port 8080).
 *
 * CORS (Cross-Origin Resource Sharing) is a security feature that
 * restricts web pages from making requests to a different domain
 * than the one that served the web page.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                // Allow requests from frontend (localhost:3000)
                .allowedOrigins(
                    "http://localhost:3000",
                    "http://127.0.0.1:3000"
                )
                // Allow all HTTP methods
                .allowedMethods(
                    "GET",
                    "POST",
                    "PUT",
                    "DELETE",
                    "OPTIONS"
                )
                // Allow all headers
                .allowedHeaders("*")
                // Allow credentials (cookies, authorization headers)
                .allowCredentials(true)
                // Cache preflight response for 1 hour
                .maxAge(3600);
    }
}
