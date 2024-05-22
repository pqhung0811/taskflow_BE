package com.example.taskflow.services;

import com.example.taskflow.entities.Comment;
import com.example.taskflow.entities.EnumState;
import com.example.taskflow.entities.FileAttachment;
import com.example.taskflow.entities.Task;
import com.example.taskflow.reponsitories.CommentRepository;
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
    @Autowired
    private CommentRepository commentRepository;

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
        List<Comment> comments;
        comments = taskRepository.findCommentByTaskId(id);
        List<FileAttachment> fileAttachments;
        fileAttachments = taskRepository.findFileByTaskId(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setComments(comments);
            task.setFileAttachments(fileAttachments);
            return task;
        } else {
            return null;
        }
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

    public void deleteTask(int id) {
        taskRepository.deleteById(id);
    }

//    public void getCommentByParent(Comment comment) {
//        List<Comment> comments = commentRepository.findCommentByParent(comment.getId());
//        if (comments.size()==0) return;
//        else {
//            comment.setReplies(comments);
//            for (Comment c : comments) {
//                getCommentByParent(c);
//            }
//        }
//    }
}
