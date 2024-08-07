package com.example.taskflow.services;

import com.example.taskflow.entities.FileAttachment;
import com.example.taskflow.entities.FileShare;
import com.example.taskflow.entities.Folder;
import com.example.taskflow.entities.Project;
import com.example.taskflow.reponsitories.FileShareRepository;
import com.example.taskflow.reponsitories.FolderRepository;
import org.apache.tomcat.jni.Local;
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
import java.time.LocalDateTime;
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
        long fileSize = multipartFile.getSize();
        Optional<Folder> optionalFolder = folderRepository.findById(folderId);
        if (optionalFolder.isPresent()) {
            Folder folder = optionalFolder.get();
            LocalDateTime updateTime = LocalDateTime.now();
            folder.setUpdateTime(updateTime);
            FileShare fileShare = new FileShare();
            fileShare.setFileName(fileName);
            Path currentPath = Paths.get("").toAbsolutePath();
            String relativePath = Paths.get("file_share", String.valueOf(projectId), folderPath).toString();
            String directoryPath = currentPath.resolve(relativePath).toString();
            String filePath = Paths.get(directoryPath, fileName).toString();
//            String directoryPath = parentPath + projectId + "/" + folderPath;
//            fileShare.setFilePath(projectId + "/" + folderPath + fileName);
            fileShare.setFilePath(filePath);
            fileShare.setFolder(folder);
            fileShare.setUpdateTime(updateTime);
            fileShare.setSize(fileSize);
            try {
                File directory = new File(directoryPath);
                if (!directory.exists()) {
                    directory.mkdirs(); // Tạo thư mục nếu chưa tồn tại
                }
                multipartFile.transferTo(new File(filePath));
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
        Path currentPath = Paths.get("").toAbsolutePath();
        String relativePath = Paths.get("file_share", String.valueOf(project.getId())).toString();
        String directoryPath = currentPath.resolve(relativePath).toString();
        String filePath = Paths.get(directoryPath, fileName).toString();
//        String directoryPath = parentPath + project.getId() + "/";
//        String filePath = directoryPath + fileName;
        LocalDateTime updateTime = LocalDateTime.now();
        long fileSize = multipartFile.getSize();
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
        fileShare.setFilePath(filePath);
        fileShare.setProject(project);
        fileShare.setUpdateTime(updateTime);
        fileShare.setSize(fileSize);
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

    public Folder saveFolder(String folderName, Project project) {
        Folder folder = new Folder();
        folder.setName(folderName);
        folder.setProject(project);
        LocalDateTime updateTime = LocalDateTime.now();
        folder.setUpdateTime(updateTime);
        folderRepository.save(folder);
        return folder;
    }

    public Folder saveFolder(String folderName, Folder parentFolder) {
        Folder folder = new Folder();
        folder.setName(folderName);
        folder.setParentFolder(parentFolder);
        LocalDateTime updateTime = LocalDateTime.now();
        folder.setUpdateTime(updateTime);
        folderRepository.save(folder);
        return folder;
    }

    public void deleteFile(FileShare fileShare) {
        File file = new File(fileShare.getFilePath());

        if (file.exists()) {
            file.delete();
        }
        fileShareRepository.delete(fileShare);
    }

    public void deleteFolder(Folder parentFolder) {
        List<Folder> folders = parentFolder.getSubFolders();
        for (Folder folder : folders) {
            deleteFolder(folder);
        }
        List<FileShare> fileShares = parentFolder.getFiles();
        for (FileShare fileShare : fileShares) {
            deleteFile(fileShare);
        }
        folderRepository.delete(parentFolder);
    }
}
