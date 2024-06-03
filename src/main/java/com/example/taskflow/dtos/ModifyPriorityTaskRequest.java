package com.example.taskflow.dtos;

import com.example.taskflow.entities.EnumPriority;
import lombok.Data;

@Data
public class ModifyPriorityTaskRequest {
    private int taskId;
    private EnumPriority newPriority;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public EnumPriority getNewPriority() {
        return newPriority;
    }

    public void setNewPriority(EnumPriority newPriority) {
        this.newPriority = newPriority;
    }
}
