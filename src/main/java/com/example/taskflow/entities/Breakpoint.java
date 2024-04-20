package com.example.taskflow.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "breakpoint")
public class Breakpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date date;
    @ManyToOne
    @JoinColumn(name = "taskId") // Tên cột khóa ngoại trỏ tới Task
    private Task task;
}
