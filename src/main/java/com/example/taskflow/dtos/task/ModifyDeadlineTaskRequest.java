package com.example.taskflow.dtos.task;

import lombok.Data;

@Data
public class ModifyDeadlineTaskRequest {
    private int taskId;
    private String newDeadline;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getNewDeadline() {
        return newDeadline;
    }

    public void setNewDeadline(String newDeadline) {
        this.newDeadline = newDeadline;
    }
}
