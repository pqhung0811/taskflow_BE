package com.example.taskflow.config.socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Định cấu hình message broker
        config.enableSimpleBroker("/topic"); // Cho phép gửi message tới tất cả các subscriber đang lắng nghe trên "/topic"
        config.setApplicationDestinationPrefixes("/app"); // Định nghĩa các prefix của các message gửi từ client lên server
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Đăng ký endpoint cho client kết nối tới WebSocket
        registry.addEndpoint("/ws").withSockJS(); // Sử dụng SockJS để hỗ trợ các trình duyệt không hỗ trợ WebSocket
    }
}

