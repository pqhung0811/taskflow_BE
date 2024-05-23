package com.example.taskflow.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "notifications")
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String text;
    private boolean isRead;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
