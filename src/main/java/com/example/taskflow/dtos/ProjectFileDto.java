package com.example.taskflow.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ProjectFileDto {
    private List<FolderDto> folder;
    private List<FileShareDto> fileShare;

    public ProjectFileDto(List<FolderDto> folder, List<FileShareDto> fileShare) {
        this.folder = folder;
        this.fileShare = fileShare;
    }
}
