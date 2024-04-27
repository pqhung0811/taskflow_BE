package com.example.taskflow.dtos;

import lombok.Data;

@Data
public class ModifyAdvanceTaskRequest {
    private int taskId;
    private int newAdvance;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getNewAdvance() {
        return newAdvance;
    }

    public void setNewAdvance(int newAdvance) {
        this.newAdvance = newAdvance;
    }
}
