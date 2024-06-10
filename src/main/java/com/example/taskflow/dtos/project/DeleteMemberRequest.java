package com.example.taskflow.dtos.project;

import lombok.Data;

@Data
public class DeleteMemberRequest {
    private int projectId;
    private int userId;
}
