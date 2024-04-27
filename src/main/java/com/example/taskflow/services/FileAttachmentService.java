package com.example.taskflow.services;

import com.example.taskflow.entities.FileAttachment;
import com.example.taskflow.reponsitories.FileAttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileAttachmentService {
//    @Autowired
//    private FileAttachmentRepository fileAttachmentRepository;
//
//    @Value("${upload.dir}")
//    private String uploadDir;
//
//    public String saveFile(MultipartFile file) {
//        // Lưu file vào thư mục trên máy chủ
//        String fileName = file.getOriginalFilename();
//        String filePath = "/D:/New folder/taskflow/file_folder/" + fileName;
//        try {
//            file.transferTo(new File(filePath));
//        } catch (IOException e) {
//            e.printStackTrace();
//            // Xử lý lỗi khi không thể lưu file
//        }
//
//        // Lưu đường dẫn file vào cơ sở dữ liệu
//        FileAttachment fileAttachment = new FileAttachment();
//        fileAttachment.setFileName(fileName);
//        fileAttachment.setFilePath(filePath);
//        fileAttachmentRepository.save(fileAttachment);
//
//        return filePath;
//    }
//
//    public Resource loadFileAsResource(String filePath) {
//        try {
//            Path file = Paths.get(filePath);
//            Resource resource = new UrlResource(file.toUri());
//            if (resource.exists() || resource.isReadable()) {
//                return resource;
//            } else {
//                throw new RuntimeException("File not found or cannot be read");
//            }
//        } catch (MalformedURLException e) {
//            throw new RuntimeException("File not found or cannot be read", e);
//        }
//    }
//
//    public FileAttachment getFileById(int id) {
//        return fileAttachmentRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found"));
//    }
}
