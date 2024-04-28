package com.example.taskflow.services;

import com.example.taskflow.entities.Comment;
import com.example.taskflow.entities.EnumState;
import com.example.taskflow.entities.Task;
import com.example.taskflow.reponsitories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getTasksByProjectId(int projectId) {
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        return tasks;
    }

    public List<Task> getTasksByUserId(int userId) {
        return taskRepository.findByUserId(userId);
    }

    public Task createTask(Task task) {
        taskRepository.save(task);
        return task;
    }

    public Task getTaskById(int id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        List<Comment> comments = new ArrayList<>();
        comments = taskRepository.findCommentByTaskId(id);
        Task task = optionalTask.get();
        task.setComments(comments);
        return task;
    }

    public Task updateTitle(int taskId, String title) {
        taskRepository.updateTitleById(taskId, title);
        Task task = getTaskById(taskId);
        return task;
    }

    public Task updateDescription(int taskId, String description) {
        taskRepository.updateDescriptionById(taskId, description);
        Task task = getTaskById(taskId);
        return task;
    }

    public Task updateAdvance(int taskId, int advance) {
        taskRepository.updateAdvanceById(taskId, advance);
        Task task = getTaskById(taskId);
        return task;
    }

    public Task updateDeadline(int taskId, String deadline) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//        LocalDateTime deadlineTime = LocalDateTime.parse(deadline, formatter);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate deadlineDate = LocalDate.parse(deadline, formatter);
        LocalDateTime deadlineTime = deadlineDate.atStartOfDay();
        taskRepository.updateDeadlineById(taskId, deadlineTime);
        Task task = getTaskById(taskId);
        return task;
    }

    public Task updateState(int taskId, EnumState newState) {
        taskRepository.updateStateById(taskId, newState);
        Task task = getTaskById(taskId);
        return task;
    }
}
