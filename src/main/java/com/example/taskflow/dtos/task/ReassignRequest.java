package com.example.taskflow.dtos.task;

import lombok.Data;

@Data
public class ReassignRequest {
    private int taskId;
    private int userId;
}
