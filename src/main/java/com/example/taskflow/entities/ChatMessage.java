package com.example.taskflow.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

public class ChatMessage {
    private int id;
    private String content;
    private int recipientId;
    private int senderId;
    private List<FileMessage> fileMessages;
}
