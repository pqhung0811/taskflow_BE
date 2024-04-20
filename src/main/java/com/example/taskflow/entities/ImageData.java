package com.example.taskflow.entities;

import javax.persistence.*;

@Entity
@Table(name = "imagedata")
public class ImageData {
    @Id
    private int id;
    private String name;
    private String type;
    @OneToOne
    private User user;
    @Lob
    @Column(name = "ImageData", length = 1000)
    private byte[] imageData;
}
