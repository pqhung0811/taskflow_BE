package com.example.taskflow.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class FileShare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fileName;
    private String filePath;
    private LocalDateTime updateTime;
    private long size;
    @ManyToOne
    private Folder folder;
    @ManyToOne
    private Project project;
}
