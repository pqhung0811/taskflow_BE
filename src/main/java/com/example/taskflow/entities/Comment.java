package com.example.taskflow.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "comment")
public class Comment {
    @Id
    private int id;
    private String content;
    private Date date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "task_id") // Tên cột khóa ngoại trỏ tới Task
    private Task task;
}
