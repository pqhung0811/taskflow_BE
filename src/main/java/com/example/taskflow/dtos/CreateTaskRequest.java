package com.example.taskflow.dtos;

import com.example.taskflow.entities.EnumState;
import lombok.Data;

import java.util.Date;

@Data
public class CreateTaskRequest {
    private String title;
    private int advance;
    private String email;
    private int projectId;
//    private String startTime;
    private String deadline;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAdvance() {
        return advance;
    }

    public void setAdvance(int advance) {
        this.advance = advance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

//    public String getStartTime() {
//        return startTime;
//    }

//    public void setStartTime(String startTime) {
//        this.startTime = startTime;
//    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
