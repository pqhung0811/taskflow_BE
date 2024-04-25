package com.example.taskflow.controllers;

import com.example.taskflow.dtos.CreateTaskRequest;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;

    @GetMapping(path = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<Task>>> getTasks(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            List<Task> tasks = taskService.getTasksByUserId(userDetails.getUser().getId());
            Map<String, List<Task>> hasMap = new HashMap<>();
            hasMap.put("tasks", tasks);
            return ResponseEntity.status(HttpStatus.OK).body(hasMap);
        }
    }

    @GetMapping(path = "/tasks/currentTask/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Task>> getTask(@PathVariable int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            Task task = taskService.getTaskById(id);
            Map<String, Task> hasMap = new HashMap<>();
            hasMap.put("task", task);
            return ResponseEntity.status(HttpStatus.OK).body(hasMap);
        }
    }

    @PostMapping(path = "/tasks/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createNewTask(@RequestBody CreateTaskRequest createTaskRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            try {
                CustomUserDetails userDetails = (CustomUserDetails) userService.loadUserByUsername(createTaskRequest.getEmail());
                Optional<Project> projectOptional = projectService.getProjectById(createTaskRequest.getProjectId());
                if (!projectOptional.isPresent()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Project is not exist");
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime startTime = LocalDateTime.parse(createTaskRequest.getStartTime(), formatter);
                LocalDateTime deadline = LocalDateTime.parse(createTaskRequest.getDeadline(), formatter);
                Project project = projectOptional.get();

                Task task = new Task(createTaskRequest.getTitle(),
                        createTaskRequest.getAdvance(),
                        startTime,
                        deadline,
                        project,
                        userDetails.getUser(),
                        createTaskRequest.getDescription());
                taskService.createTask(task);
                return ResponseEntity.status(HttpStatus.OK).body("Create task successfully");
            }
            catch (UsernameNotFoundException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User is not found");
            }
        }
    }

//    @PatchMapping(path = "/tasks/modified/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity modifyTask() {
//
//    }
}
