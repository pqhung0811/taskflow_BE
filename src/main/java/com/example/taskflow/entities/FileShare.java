package com.example.taskflow.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class FileShare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fileName;
    private String filePath;
    @ManyToOne
    private Folder folder;
    @ManyToOne
    private Project project;
}
