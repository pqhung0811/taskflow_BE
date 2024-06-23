package com.example.taskflow.dtos;

import com.example.taskflow.entities.FileShare;
import com.example.taskflow.entities.Folder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class FolderDto {
    private int id;
    private String folderName;
    private LocalDateTime updateTime;
    private List<SubFolderDto> subFolders = new ArrayList<>();
    private List<FileShareDto> fileShares = new ArrayList<>();

    public FolderDto() {
    }

    public FolderDto(Folder folder) {
        this.id = folder.getId();
        this.folderName = folder.getName();
        this.updateTime = folder.getUpdateTime();
        for (FileShare fileShare : folder.getFiles()) {
            FileShareDto fileShareDto = new FileShareDto(fileShare);
            this.fileShares.add(fileShareDto);
        }
        for (Folder folder1 : folder.getSubFolders()) {
            SubFolderDto subFolderDto = new SubFolderDto(folder1);
            this.subFolders.add(subFolderDto);
        }
    }
}
