//package com.example.taskflow.controllers;
//
//public class ChatController {
//    @MessageMapping("/chat")
//    @SendTo("/topic/messages")
//    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
//        // Xử lý tin nhắn và trả về nó
//        return chatMessage;
//    }
//
//    @MessageMapping("/file")
//    @SendTo("/topic/messages")
//    public ChatMessage sendFile(@Payload ChatMessage chatMessage) {
//        // Xử lý tin nhắn chứa file và trả về nó
//        return chatMessage;
//    }
//
//}
