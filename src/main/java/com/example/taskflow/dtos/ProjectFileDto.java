package com.example.taskflow.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ProjectFileDto {
    private List<SubFolderDto> folder;
    private List<FileShareDto> fileShare;

    public ProjectFileDto(List<SubFolderDto> folder, List<FileShareDto> fileShare) {
        this.folder = folder;
        this.fileShare = fileShare;
    }
}
