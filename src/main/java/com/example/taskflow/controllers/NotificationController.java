package com.example.taskflow.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class NotificationController {
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/message/{id}")
    @SendTo("/topic/messages/{id}")
    public String sendMessage(@PathVariable int id, String message) {
        System.out.println();
        return message;
    }

    public void sendNotification(int id, String message) {
        System.out.println("lmao code 1 " + message);
        messagingTemplate.convertAndSend("/topic/messages/" + id, message);
    }
}
