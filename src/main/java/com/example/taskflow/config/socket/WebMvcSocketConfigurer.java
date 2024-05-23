//package com.example.taskflow.config.socket;
//
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//public class WebMvcSocketConfigurer implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000")
//                .allowCredentials(false)
//                .maxAge(3600)
//                .allowedHeaders("Accept", "Content-Type", "Origin",
//                        "Authorization", "X-Auth-Token")
//                .exposedHeaders("X-Auth-Token", "Authorization")
//                .allowedMethods("POST", "GET", "DELETE", "PUT", "OPTIONS");
//    }
//}
