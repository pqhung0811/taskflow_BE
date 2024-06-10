package com.example.taskflow.dtos.fileshare;

import lombok.Data;

@Data
public class FolderRequest {
    private String folderName;
    private int projectId;
    private int parentId;
}
