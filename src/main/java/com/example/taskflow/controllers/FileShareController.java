package com.example.taskflow.controllers;

import com.example.taskflow.dtos.FileShareDto;
import com.example.taskflow.dtos.FolderDto;
import com.example.taskflow.dtos.ProjectFileDto;
import com.example.taskflow.dtos.fileshare.FolderRequest;
import com.example.taskflow.entities.FileAttachment;
import com.example.taskflow.entities.FileShare;
import com.example.taskflow.entities.Folder;
import com.example.taskflow.entities.Project;
import com.example.taskflow.services.FileShareService;
import com.example.taskflow.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class FileShareController {
    @Autowired
    private FileShareService fileShareService;
    @Autowired
    private ProjectService projectService;

    @PostMapping(path = "/project/addFile")
    public ResponseEntity<?> addFileShareProject(@RequestParam("fileShare") MultipartFile file,
                                                 @RequestParam("projectId") int projectId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            Optional<Project> projectOptional = projectService.getProjectById(projectId);
            if (!projectOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");
            }
            Project project = projectOptional.get();
            FileShare fileShare = fileShareService.saveFileToProject(file, project);
            FileShareDto fileShareDto = new FileShareDto(fileShare);
            return ResponseEntity.status(HttpStatus.OK).body(fileShareDto);
        }
    }

    @PostMapping(path = "/folder/addFile")
    public ResponseEntity<?> addFileShareFolder(@RequestParam("fileShare") MultipartFile file,
                                                 @RequestParam("folderId") int folderId,
                                                @RequestParam("projectId") int projectId,
                                                @RequestParam("folderPath") String folderPath) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            FileShare fileShare = fileShareService.saveFileToFolder(file, folderId, projectId, folderPath);
            FileShareDto fileShareDto = new FileShareDto(fileShare);
            return ResponseEntity.status(HttpStatus.OK).body(fileShareDto);
        }
    }

    @GetMapping(path = "/project/file/{id}")
    public ResponseEntity<?> getFile(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            Optional<Project> projectOptional = projectService.getProjectById(id);
            if (!projectOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");
            }
            Project project = projectOptional.get();
            List<FileShare> fileShares = fileShareService.getFileSharesByProject(project);
            List<Folder> folders = fileShareService.getFolderByProject(project);
            List<FolderDto> folderDtos = new ArrayList<>();
            List<FileShareDto> fileShareDtos = new ArrayList<>();
            for (FileShare fileShare : fileShares) {
                FileShareDto fileShareDto = new FileShareDto(fileShare);
                fileShareDtos.add(fileShareDto);
            }
            for (Folder folder : folders) {
                FolderDto folderDto = new FolderDto(folder);
                folderDtos.add(folderDto);
            }
            ProjectFileDto projectFileDto = new ProjectFileDto(folderDtos, fileShareDtos);
            return ResponseEntity.status(HttpStatus.OK).body(projectFileDto);
        }
    }

    @GetMapping(path = "/folder/file/{folderId}")
    public ResponseEntity<?> getFolderFile(@PathVariable int folderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            Folder folder = fileShareService.getFolderById(folderId);
            FolderDto folderDto = new FolderDto(folder);
            return ResponseEntity.status(HttpStatus.OK).body(folderDto);
        }
    }

    @GetMapping("/fileshare/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable int id) throws IOException {
        // Tải file từ service
        FileShare fileShare = fileShareService.getFileById(id);

        // Nếu file không tồn tại, trả về ResponseEntity 404 Not Found
        if (fileShare == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("file not found");
        }

        // Tạo Resource từ đường dẫn file
        Resource resource;
        try {
            String parentPath = "D:/New folder/taskflow/file_share/";
            resource = fileShareService.loadFileAsResource(parentPath + fileShare.getFilePath());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // Trả về Response chứa file
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileShare.getFileName() + "\"")
                .body(resource);
    }

//    @PostMapping(path = "/project/addFolder")
//    public ResponseEntity<?> addFolder(@RequestBody FolderRequest folderRequest) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        } else {
//            if (folderRequest.getProjectId()<=0) {
//
//            }
//
//        }
//    }

}
