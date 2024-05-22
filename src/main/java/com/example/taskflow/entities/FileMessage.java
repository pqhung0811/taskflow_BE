package com.example.taskflow.entities;

import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
public class FileMessage {
    private int id;
    private String filename;
    private String filePath;
    private ChatMessage chatMessage;
}
