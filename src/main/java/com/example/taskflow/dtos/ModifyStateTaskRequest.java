package com.example.taskflow.dtos;

import com.example.taskflow.entities.EnumState;
import lombok.Data;

@Data
public class ModifyStateTaskRequest {
    private int taskId;
    private EnumState newState;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public EnumState getNewState() {
        return newState;
    }

    public void setNewState(EnumState newState) {
        this.newState = newState;
    }
}
