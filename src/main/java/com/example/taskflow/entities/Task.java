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
    private String description;
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Breakpoint> breakPoints = new ArrayList<Breakpoint>();
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<Comment>();
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User responsible;

    public Task() {
    }

    public Task(String title, int advance, LocalDateTime startTime,
                LocalDateTime deadline, Project project, User responsible, String description) {
        this.title = title;
        this.advance = advance;
        this.state = EnumState.ON_HOLD;
        this.startTime = startTime;
        this.deadline = deadline;
        this.project = project;
        this.responsible = responsible;
        this.description = description;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
}
