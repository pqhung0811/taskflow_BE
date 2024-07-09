package com.example.taskflow.controllers;

import com.example.taskflow.dtos.FileShareDto;
import com.example.taskflow.dtos.FolderDto;
import com.example.taskflow.dtos.ProjectFileDto;
import com.example.taskflow.dtos.SubFolderDto;
import com.example.taskflow.dtos.fileshare.FolderRequest;
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
            List<SubFolderDto> subFolderDtos = new ArrayList<>();
            List<FileShareDto> fileShareDtos = new ArrayList<>();
            for (FileShare fileShare : fileShares) {
                FileShareDto fileShareDto = new FileShareDto(fileShare);
                fileShareDtos.add(fileShareDto);
            }
            for (Folder folder : folders) {
                SubFolderDto subFolderDto = new SubFolderDto(folder);
                subFolderDtos.add(subFolderDto);
            }
            ProjectFileDto projectFileDto = new ProjectFileDto(subFolderDtos, fileShareDtos);
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
            resource = fileShareService.loadFileAsResource(fileShare.getFilePath());
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

    @GetMapping(path = "/folder/back/{folderId}")
    public ResponseEntity<?> getFolderBack(@PathVariable int folderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            Folder folder = fileShareService.getFolderById(folderId);
            if (folder.getParentFolder()==null) {
                Project project = folder.getProject();
                List<FileShare> fileShares = fileShareService.getFileSharesByProject(project);
                List<Folder> folders = fileShareService.getFolderByProject(project);
                List<SubFolderDto> folderDtos = new ArrayList<>();
                List<FileShareDto> fileShareDtos = new ArrayList<>();
                for (FileShare fileShare : fileShares) {
                    FileShareDto fileShareDto = new FileShareDto(fileShare);
                    fileShareDtos.add(fileShareDto);
                }
                for (Folder folder1 : folders) {
                    SubFolderDto folderDto = new SubFolderDto(folder1);
                    folderDtos.add(folderDto);
                }
                ProjectFileDto projectFileDto = new ProjectFileDto(folderDtos, fileShareDtos);
                return ResponseEntity.status(HttpStatus.OK).body(projectFileDto);
            }
            else {
                Folder parentFolder = folder.getParentFolder();
                FolderDto folderDto = new FolderDto(parentFolder);
                return ResponseEntity.status(HttpStatus.OK).body(folderDto);
            }
        }
    }

    @PostMapping(path = "/folder/addFolder")
    public ResponseEntity<?> addFolder(@RequestBody FolderRequest folderRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            if (folderRequest.getParentId()==0) {
                Optional<Project> projectOptional = projectService.getProjectById(folderRequest.getProjectId());
                Project project = projectOptional.get();
                Folder folder = fileShareService.saveFolder(folderRequest.getFolderName(), project);
                FolderDto folderDto = new FolderDto(folder);
                return ResponseEntity.status(HttpStatus.OK).body(folderDto);
            } else if (folderRequest.getProjectId()==0) {
                Folder parentFolder = fileShareService.getFolderById(folderRequest.getParentId());
                Folder folder = fileShareService.saveFolder(folderRequest.getFolderName(), parentFolder);
                SubFolderDto folderDto = new SubFolderDto(folder);
                return ResponseEntity.status(HttpStatus.OK).body(folderDto);
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }
    }

    @DeleteMapping("/fileShare/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable int id) throws IOException {
        FileShare fileShare = fileShareService.getFileById(id);

        if (fileShare == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("file not found");
        }
        FileShareDto fileShareDto = new FileShareDto(fileShare);
        fileShareService.deleteFile(fileShare);
        return ResponseEntity.status(HttpStatus.OK).body(fileShareDto);
    }

    @DeleteMapping("/folder/{id}")
    public ResponseEntity<?> deleteFolder(@PathVariable int id) throws IOException {
        Folder folder = fileShareService.getFolderById(id);

        if (folder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("file not found");
        }
        SubFolderDto subFolderDto = new SubFolderDto(folder);
        fileShareService.deleteFolder(folder);
        return ResponseEntity.status(HttpStatus.OK).body(subFolderDto);
    }
}
