package com.example.taskflow.dtos;

import com.example.taskflow.entities.Folder;
import lombok.Data;

@Data
public class SubFolderDto {
    private int id;
    private String folderName;

    public SubFolderDto(Folder folder) {
        this.id = folder.getId();
        this.folderName = folder.getName();
    }
}
