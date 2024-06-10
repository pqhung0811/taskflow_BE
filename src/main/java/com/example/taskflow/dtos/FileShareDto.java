package com.example.taskflow.dtos;

import com.example.taskflow.entities.FileShare;
import lombok.Data;

@Data
public class FileShareDto {
    private int id;
    private String filename;

    public FileShareDto() {

    }

    public FileShareDto(FileShare fileShare) {
        this.id = fileShare.getId();
        this.filename = fileShare.getFileName();
    }
}
