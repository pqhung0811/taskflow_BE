package com.example.taskflow.dtos;

import lombok.Data;

@Data
public class ModifyDescriptionTaskRequest {
    private int taskId;
    private String newDescription;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getNewDescription() {
        return newDescription;
    }

    public void setNewDescription(String newDescription) {
        this.newDescription = newDescription;
    }
}
