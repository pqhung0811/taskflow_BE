package com.example.taskflow.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private int advance;
    private EnumState state;
    private LocalDateTime startTime;
    private LocalDateTime deadline;
    private LocalDateTime endTime;
    private String description;
    private EnumPriority priority;
    private EnumCategory category;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Comment> comments;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User responsible;
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<FileAttachment> fileAttachments = new ArrayList<FileAttachment>();
    private int estimateTime;

    public Task() {
    }

    public Task(String title, int advance, LocalDateTime startTime, LocalDateTime deadline,
                Project project, User responsible, String description, EnumPriority priority,
                EnumCategory category) {
        this.title = title;
        this.advance = advance;
        this.state = EnumState.ON_HOLD;
        this.startTime = startTime;
        this.deadline = deadline;
        this.project = project;
        this.responsible = responsible;
        this.description = description;
        this.priority = priority;
        this.category = category;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
}
