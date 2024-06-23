package com.example.taskflow.dtos;

import com.example.taskflow.entities.FileShare;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileShareDto {
    private int id;
    private String filename;
    private LocalDateTime updateTime;
    private long size;

    public FileShareDto() {

    }

    public FileShareDto(FileShare fileShare) {
        this.id = fileShare.getId();
        this.filename = fileShare.getFileName();
        this.updateTime = fileShare.getUpdateTime();
        this.size = fileShare.getSize();
    }
}
