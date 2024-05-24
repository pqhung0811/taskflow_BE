package com.example.taskflow.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public String sendMessage(String message) {
        return message;
    }

    public void sendNotification(String message) {
        System.out.println("lmao code 1 " + message);
        messagingTemplate.convertAndSend("/topic/messages", message);
    }
}
