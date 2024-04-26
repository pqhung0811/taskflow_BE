package com.example.taskflow.controllers;

import com.example.taskflow.dtos.CreateProjectRequest;
import com.example.taskflow.dtos.JoinMemberRequest;
import com.example.taskflow.entities.Project;
import com.example.taskflow.entities.Task;
import com.example.taskflow.entities.User;
import com.example.taskflow.services.CustomUserDetails;
import com.example.taskflow.services.ProjectService;
import com.example.taskflow.services.TaskService;
import com.example.taskflow.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    @GetMapping(path = "/projects", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<Project>>> getProjects() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            List<Project> projects = projectService.getProjectsByUserId(customUserDetails.getUser().getId());
            Map<String, List<Project>> hashMap = new HashMap<>();
            hashMap.put("projects", projects);
            return ResponseEntity.status(HttpStatus.OK).body(hashMap);
        }
    }

    @GetMapping(path = "/projects/{id}/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<Task>>> getTasks(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            List<Task> tasks = taskService.getTasksByProjectId(id);
            Map<String, List<Task>> hasMap = new HashMap<>();
            hasMap.put("tasks", tasks);
            return ResponseEntity.status(HttpStatus.OK).body(hasMap);
        }
    }

    @GetMapping(path = "/projects/{projectId}/members", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<User>>> getMembers(@PathVariable int projectId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            List<User> users = projectService.getUserByProjectId(projectId);
            Map<String, List<User>> hasMap = new HashMap<>();
            hasMap.put("members", users);
            return ResponseEntity.status(HttpStatus.OK).body(hasMap);
        }
    }

    @PostMapping(path = "/projects/joinMember", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addMemberToProject(@RequestBody JoinMemberRequest joinMemberRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            try {
                CustomUserDetails userDetails = (CustomUserDetails) userService.loadUserByUsername(joinMemberRequest.getEmail());
                Optional<Project> projectOptional = projectService.getProjectById(joinMemberRequest.getId());
                Project project = projectOptional.get();
                User user = userDetails.getUser();
                if (!projectService.checkMemberInProject(user, project)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("User is available in project");
                }
                projectService.addMemberToProject(user, project);
                return ResponseEntity.status((HttpStatus.OK)).body("Add member successfully");
            }
            catch (UsernameNotFoundException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User is not found");
            }
        }
    }

    @PostMapping(path = "/projects/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Project>> createProject(@RequestBody CreateProjectRequest createProjectRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            LocalDateTime startDate = LocalDateTime.now();
            System.out.println("lmao create prj: " + customUserDetails.getUser().getEmail());
            Project project = projectService.createProject(createProjectRequest.getTitle(), startDate, customUserDetails.getUser());
            Map<String, Project> hasMap = new HashMap<>();
            hasMap.put("project", project);
            return ResponseEntity.status((HttpStatus.OK)).body(hasMap);
        }
    }

//    @DeleteMapping(path = "/projects/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity deleteProject(@PathVariable int projectId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        }
//        projectService.deleteProject(projectId);
//        return ResponseEntity.status((HttpStatus.OK)).body("Delete project successfully");
//    }

}
