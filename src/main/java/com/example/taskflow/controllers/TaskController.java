package com.example.taskflow.controllers;

import com.example.taskflow.dtos.*;
import com.example.taskflow.entities.*;
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
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private FileAttachmentService fileAttachmentService;
    @Autowired
    private NotificationController notificationController;

    @GetMapping(path = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            System.out.println("lmao task controller : ");
            notificationController.sendNotification("a");
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
    public ResponseEntity<?> getTask(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
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
        } else {
            try {
                Map<String, Task> hashMap = new HashMap<>();
                CustomUserDetails userDetails = (CustomUserDetails) userService.loadUserByUsername(createTaskRequest.getEmail());
                Optional<Project> projectOptional = projectService.getProjectById(createTaskRequest.getProjectId());
                if (!projectOptional.isPresent()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("project is not exist");
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
            } catch (UsernameNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user is not exist");
            }
        }
    }

    @PatchMapping(path = "/tasks/modifyTitle", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTitleTask(@RequestBody ModifyTitleTaskRequest modifyTitleTaskRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
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
        } else {
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
        } else {
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
        } else {
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
        } else {
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
        } else {
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

    @PostMapping(path = "/tasks/addFile")
    public ResponseEntity<?> addFileAttachment(@RequestParam("file") MultipartFile file, @RequestParam("id") int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            if (file == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("no file");
            }
            Task task = taskService.getTaskById(id);
            if (task == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no task found");
            }
            FileAttachment fileAttachment = fileAttachmentService.saveFile(file, task);

//            TaskDto taskDto = new TaskDto(taskService.getTaskById(task.getId()));
            FileAttachmentDto fileAttachmentDto = new FileAttachmentDto(fileAttachment);
            return ResponseEntity.status(HttpStatus.OK).body(fileAttachmentDto);
        }
    }

    @DeleteMapping(path= "/tasks/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            taskService.deleteTask(id);
            fileAttachmentService.deleteDirectory(id);
            return ResponseEntity.status(HttpStatus.OK).body(id);
        }
    }
}
