//package com.example.taskflow.config.socket;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@EnableWebMvc
//public class WebMvcSocketConfigurer implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/ws/**")
//                .allowedOrigins("http://localhost:3000")
//                .allowCredentials(true)
//                .maxAge(3600)
//                .allowedMethods("POST", "GET", "DELETE", "PUT", "OPTIONS");
//    }
//}