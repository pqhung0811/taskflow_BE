package com.example.taskflow.controllers;

import com.example.taskflow.entities.FileAttachment;
import com.example.taskflow.services.FileAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class FileAttachmentController {
    @Autowired
    private FileAttachmentService fileAttachmentService;

    @GetMapping("/file/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable int id) throws IOException {
        // Tải file từ service
        FileAttachment fileAttachment = fileAttachmentService.getFileById(id);

        // Nếu file không tồn tại, trả về ResponseEntity 404 Not Found
        if (fileAttachment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("file not found");
        }

        // Tạo Resource từ đường dẫn file
        Resource resource;
        try {
            resource = fileAttachmentService.loadFileAsResource(fileAttachment.getFilePath());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // Trả về Response chứa file
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileAttachment.getFileName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/file/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable int id) throws IOException {
        FileAttachment fileAttachment = fileAttachmentService.getFileById(id);

        if (fileAttachment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("file not found");
        }
        fileAttachmentService.deleteFile(fileAttachment);
        return ResponseEntity.status(HttpStatus.OK).body("delete file successfully");
    }
}
