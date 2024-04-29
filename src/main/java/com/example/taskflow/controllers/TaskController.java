package com.example.taskflow.controllers;

import com.example.taskflow.dtos.*;
import com.example.taskflow.entities.Comment;
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

import java.text.DateFormat;
import java.text.ParseException;
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
    @Autowired
    private CommentService commentService;

    @GetMapping(path = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTasks(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            List<Task> tasks = taskService.getTasksByUserId(userDetails.getUser().getId());
            List<TaskDto> taskDtos = new ArrayList<>();
            for (Task t : tasks) {
                TaskDto taskDto = new TaskDto(t);
                taskDtos.add(taskDto);
            }
            Map<String, List<TaskDto>> hasMap = new HashMap<>();
            hasMap.put("tasks", taskDtos);
            return ResponseEntity.status(HttpStatus.OK).body(hasMap);
        }
    }

    @GetMapping(path = "/tasks/currentTask/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTask(@PathVariable int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            Task task = taskService.getTaskById(id);
            TaskDto taskDto = new TaskDto(task);
            Map<String, TaskDto> hasMap = new HashMap<>();
            hasMap.put("task", taskDto);
            return ResponseEntity.status(HttpStatus.OK).body(hasMap);
        }
    }

    @PostMapping(path = "/tasks/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewTask(@RequestBody CreateTaskRequest createTaskRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            try {
                Map<String, Task> hashMap = new HashMap<>();
                CustomUserDetails userDetails = (CustomUserDetails) userService.loadUserByUsername(createTaskRequest.getEmail());
                Optional<Project> projectOptional = projectService.getProjectById(createTaskRequest.getProjectId());
                if (!projectOptional.isPresent()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("project is not exist");
                }
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                LocalDateTime startTime = LocalDateTime.now();
                LocalDateTime deadline = LocalDateTime.parse(createTaskRequest.getDeadline(), formatter);
                Project project = projectOptional.get();

                Task task = new Task(createTaskRequest.getTitle(),
                        createTaskRequest.getAdvance(),
                        startTime,
                        deadline,
                        project,
                        userDetails.getUser(),
                        createTaskRequest.getDescription());
                task = taskService.createTask(task);
                hashMap.put("task", task);

                return ResponseEntity.status(HttpStatus.OK).body(hashMap);
            }
            catch (UsernameNotFoundException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("user is not exist");
            }
        }
    }

    @PatchMapping(path = "/tasks/modifyTitle", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTitleTask(@RequestBody ModifyTitleTaskRequest modifyTitleTaskRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            Task task = taskService.updateTitle(modifyTitleTaskRequest.getTaskId(), modifyTitleTaskRequest.getNewTitle());
            Map<String, Task> hasMap = new HashMap<>();
            hasMap.put("task", task);
            return ResponseEntity.status(HttpStatus.OK).body(hasMap);
        }
    }

    @PatchMapping(path = "/tasks/modifyDescription", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateDescriptionTask(@RequestBody ModifyDescriptionTaskRequest modifyDescriptionTaskRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            Task task = taskService.updateDescription(modifyDescriptionTaskRequest.getTaskId(), modifyDescriptionTaskRequest.getNewDescription());
            Map<String, Task> hasMap = new HashMap<>();
            hasMap.put("task", task);
            return ResponseEntity.status(HttpStatus.OK).body(hasMap);
        }
    }

    @PatchMapping(path = "/tasks/modifyProgress", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAdvanceTask(@RequestBody ModifyAdvanceTaskRequest modifyAdvanceTaskRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            Task task = taskService.updateAdvance(modifyAdvanceTaskRequest.getTaskId(), modifyAdvanceTaskRequest.getNewAdvance());
            Map<String, Task> hasMap = new HashMap<>();
            hasMap.put("task", task);
            return ResponseEntity.status(HttpStatus.OK).body(hasMap);
        }
    }

    @PatchMapping(path = "/tasks/modifyDeadline", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateDeadlineTask(@RequestBody ModifyDeadlineTaskRequest modifyDeadlineTaskRequest) throws ParseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            Task task = taskService.updateDeadline(modifyDeadlineTaskRequest.getTaskId(), modifyDeadlineTaskRequest.getNewDeadline());
            Map<String, Task> hasMap = new HashMap<>();
            hasMap.put("task", task);
            return ResponseEntity.status(HttpStatus.OK).body(hasMap);
        }
    }

    @PatchMapping(path = "/tasks/modifyState", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateStateTask(@RequestBody ModifyStateTaskRequest modifyStateTaskRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            Task task = taskService.updateState(modifyStateTaskRequest.getTaskId(), modifyStateTaskRequest.getNewState());
            ProjectsTaskDto projectsTaskDto = new ProjectsTaskDto(task);
            Map<String, ProjectsTaskDto> hasMap = new HashMap<>();
            hasMap.put("task", projectsTaskDto);
            return ResponseEntity.status(HttpStatus.OK).body(hasMap);
        }
    }

    @PostMapping(path = "/tasks/comment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> commentTask(@RequestBody CommentRequest commentRequest) throws ParseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();
            Task task = taskService.getTaskById(commentRequest.getTaskId());
            Comment comment = commentService.createComment(user, commentRequest.getText(), task);
            task.addComment(comment);
            Map<String, TaskDto> response = new HashMap<>();
            TaskDto taskDto = new TaskDto(task);
            response.put("task", taskDto);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

}
