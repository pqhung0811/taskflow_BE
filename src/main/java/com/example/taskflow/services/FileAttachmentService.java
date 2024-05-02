package com.example.taskflow.services;

import com.example.taskflow.entities.FileAttachment;
import com.example.taskflow.entities.Task;
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
import java.util.Optional;

@Service
public class FileAttachmentService {
    @Autowired
    private FileAttachmentRepository fileAttachmentRepository;

    public String saveFile(MultipartFile file, Task task) {
        String fileName = file.getOriginalFilename();
        String directoryPath = "D:/New folder/taskflow/file_folder/task" + task.getId() + "/";
        String filePath = directoryPath + fileName;
        try {
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs(); // Tạo thư mục nếu chưa tồn tại
            }
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileAttachment fileAttachment = new FileAttachment();
        fileAttachment.setFileName(fileName);
        fileAttachment.setFilePath(filePath);
        fileAttachment.setTask(task);
        fileAttachmentRepository.save(fileAttachment);

        return filePath;
    }

    public Resource loadFileAsResource(String filePath) throws IOException {
        try {
            Path file = Paths.get(filePath);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found or cannot be read");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("File not found or cannot be read", e);
        }
    }

    public FileAttachment getFileById(int id) {
        Optional<FileAttachment> optionalFileAttachment = fileAttachmentRepository.findById(id);
        return optionalFileAttachment.orElse(null);
    }

    public void deleteFile(FileAttachment fileAttachment) {
        File file = new File(fileAttachment.getFilePath());

        if (file.exists()) {
            file.delete();
        }
        fileAttachmentRepository.delete(fileAttachment);
    }
}
