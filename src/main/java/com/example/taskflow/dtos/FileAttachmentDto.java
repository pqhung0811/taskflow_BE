package com.example.taskflow.dtos;

import com.example.taskflow.entities.FileAttachment;
import lombok.Data;

import java.io.File;

@Data
public class FileAttachmentDto {
    private int id;
    private String filename;

    public FileAttachmentDto() {

    }

    public FileAttachmentDto(FileAttachment fileAttachment) {
        this.id = fileAttachment.getId();
        this.filename = fileAttachment.getFileName();
    }
}
