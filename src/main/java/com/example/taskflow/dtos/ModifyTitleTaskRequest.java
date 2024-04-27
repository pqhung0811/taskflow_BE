package com.example.taskflow.dtos;

import lombok.Data;

@Data
public class ModifyTitleTaskRequest {
    private int taskId;
    private String newTitle;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getNewTitle() {
        return newTitle;
    }

    public void setNewTitle(String newTitle) {
        this.newTitle = newTitle;
    }
}
