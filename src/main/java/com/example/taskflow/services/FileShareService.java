package com.example.taskflow.services;

import com.example.taskflow.entities.FileShare;
import com.example.taskflow.entities.Folder;
import com.example.taskflow.entities.Project;
import com.example.taskflow.reponsitories.FileShareRepository;
import com.example.taskflow.reponsitories.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class FileShareService {
    @Autowired
    private FileShareRepository fileShareRepository;
    @Autowired
    private FolderRepository folderRepository;
    private String parentPath = "D:/New folder/taskflow/file_share/";

    public FileShare saveFileToFolder(MultipartFile multipartFile, int folderId, int projectId, String folderPath) {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Optional<Folder> optionalFolder = folderRepository.findById(folderId);
        if (optionalFolder.isPresent()) {
            Folder folder = optionalFolder.get();

            FileShare fileShare = new FileShare();
            fileShare.setFileName(fileName);
            String directoryPath = parentPath + projectId + "/" + folderPath;
            fileShare.setFilePath(projectId + "/" + folderPath + fileName);
            fileShare.setFolder(folder);

            try {
                File directory = new File(directoryPath);
                if (!directory.exists()) {
                    directory.mkdirs(); // Tạo thư mục nếu chưa tồn tại
                }
                multipartFile.transferTo(new File(parentPath + fileShare.getFilePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return fileShareRepository.save(fileShare);
        } else {
            System.out.println("Folder not found with id: " + folderId);
            return null;
        }
    }

    public FileShare saveFileToProject(MultipartFile multipartFile, Project project) {
        String fileName = multipartFile.getOriginalFilename();
        String directoryPath = parentPath + project.getId() + "/";
        String filePath = directoryPath + fileName;
        try {
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs(); // Tạo thư mục nếu chưa tồn tại
            }
            multipartFile.transferTo(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileShare fileShare = new FileShare();
        fileShare.setFileName(fileName);
        fileShare.setFilePath(project.getId() + "/" + fileName);
        fileShare.setProject(project);
        fileShareRepository.save(fileShare);

        return fileShare;
    }

    public List<FileShare> getFileSharesByProject(Project project) {
        return fileShareRepository.findByProject(project);
    }

    public List<Folder> getFolderByProject(Project project) {
        return folderRepository.findByProject(project);
    }

    public FileShare getFileById(int id) {
        Optional<FileShare> optionalFileShare = fileShareRepository.findById(id);
        FileShare fileShare = optionalFileShare.get();
        return fileShare;
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

    public Folder getFolderById(int folderId) {
        Optional<Folder> folderOptional = folderRepository.findById(folderId);
        return folderOptional.get();
    }

    public Folder saveFolder(String folderName, Project project, String parentPath) {
        return null;
    }
}
