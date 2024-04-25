package com.example.taskflow.services;

import com.example.taskflow.entities.Task;
import com.example.taskflow.reponsitories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getTasksByProjectId(int projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public List<Task> getTasksByUserId(int userId) {
        return taskRepository.findByUserId(userId);
    }

    public void createTask(Task task) {
        taskRepository.save(task);
    }

    public Task getTaskById(int id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        Task task = optionalTask.get();
        return task;
    }
}
