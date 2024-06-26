package com.example.taskflow.controllers;

import com.example.taskflow.dtos.*;
import com.example.taskflow.dtos.project.*;
import com.example.taskflow.entities.Project;
import com.example.taskflow.entities.Task;
import com.example.taskflow.entities.User;
import com.example.taskflow.services.*;
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
import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationsService notificationsService;
    @Autowired
    private NotificationController notificationController;

    @GetMapping(path = "/projects", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProjects() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            List<Project> projects = projectService.getProjectsByUserId(customUserDetails.getUser().getId());
            List<ProjectDto> projectDtos = new ArrayList<>();
            for (Project p : projects) {
                ProjectDto projectDto = new ProjectDto(p);
                projectDtos.add(projectDto);
            }
            Map<String, List<ProjectDto>> hashMap = new HashMap<>();
            hashMap.put("projects", projectDtos);
            return ResponseEntity.status(HttpStatus.OK).body(hashMap);
        }
    }

    @GetMapping(path = "/projects/{id}/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTasks(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            List<Task> tasks = taskService.getTasksByProjectId(id);
            List<ProjectsTaskDto> projectsTaskDtos = new ArrayList<>();
            for (Task t : tasks) {
                ProjectsTaskDto projectsTaskDto = new ProjectsTaskDto(t);
                projectsTaskDtos.add(projectsTaskDto);
            }
            Map<String, List<ProjectsTaskDto>> hasMap = new HashMap<>();
            hasMap.put("tasks", projectsTaskDtos);
            return ResponseEntity.status(HttpStatus.OK).body(hasMap);
        }
    }

    @GetMapping(path = "/projects/{projectId}/members", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMembers(@PathVariable int projectId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            List<User> users = projectService.getUserByProjectId(projectId);
            Map<String, List<UserDto>> hasMap = new HashMap<>();
            List<UserDto> userDtos = new ArrayList<>();
            for (User u : users) {
                UserDto userDto = new UserDto(u);
                userDtos.add(userDto);
            }
            hasMap.put("members", userDtos);
            return ResponseEntity.status(HttpStatus.OK).body(hasMap);
        }
    }

    @PostMapping(path = "/projects/joinMember", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addMemberToProject(@RequestBody JoinMemberRequest joinMemberRequest) {
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
                String textNotify = "You are added to new project (" + project.getName() + ")";
                notificationsService.createNotification(user, textNotify);
                notificationController.sendNotification(user.getId(), "a");
                projectService.addMemberToProject(user, project);
                UserDto userDto = new UserDto(user);
                Map<String, UserDto> hasMap = new HashMap<>();
                hasMap.put("member", userDto);
                return ResponseEntity.status((HttpStatus.OK)).body(hasMap);
            }
            catch (UsernameNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User is not found");
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
            Project project = projectService.createProject(createProjectRequest.getTitle(), startDate, customUserDetails.getUser());
            Map<String, Project> hasMap = new HashMap<>();
            hasMap.put("project", project);
            return ResponseEntity.status((HttpStatus.OK)).body(hasMap);
        }
    }

    @DeleteMapping(path = "/projects/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteProject(@PathVariable int projectId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        projectService.deleteProject(projectId);
        return ResponseEntity.status((HttpStatus.OK)).body("Delete project successfully");
    }

    @PatchMapping(path= "/projects/deleteMember")
    public ResponseEntity removeMember(@RequestBody DeleteMemberRequest deleteMemberRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Optional<Project> projectOptional = projectService.getProjectById(deleteMemberRequest.getProjectId());
        Project project = projectOptional.get();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        if (customUserDetails.getUser().getId() != project.getProjectManager().getId()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not PM");
        }
        projectService.removeMember(deleteMemberRequest.getUserId(), project);
        String textNotify = "You are removed to project (" + project.getName() + ")";
        User user = userService.getUserById(deleteMemberRequest.getUserId());
        notificationsService.createNotification(user, textNotify);
        notificationController.sendNotification(user.getId(), "a");
        ProjectDto projectDto = new ProjectDto(project);
        return ResponseEntity.status(HttpStatus.OK).body(projectDto);
    }

}
