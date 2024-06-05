package com.example.taskflow.dtos.project;

import com.example.taskflow.dtos.*;
import com.example.taskflow.entities.Comment;
import com.example.taskflow.entities.EnumState;
import com.example.taskflow.entities.Task;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProjectsTaskDto {
    private int id;
    private String title;
    private int advance;
    private EnumState state;
    private LocalDateTime startTime;
    private LocalDateTime deadline;
    private String description;
    private UserDto responsible;
    private ProjectDto project;

    public ProjectsTaskDto(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.advance = task.getAdvance();
        this.state = task.getState();
        this.startTime = task.getStartTime();
        this.deadline = task.getDeadline();
        this.description = task.getDescription();
        UserDto userDto = new UserDto();
        userDto.setId(task.getResponsible().getId());
        userDto.setEmail(task.getResponsible().getEmail());
        userDto.setName(task.getResponsible().getName());
        this.responsible = userDto;
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(task.getProject().getId());
        projectDto.setName(task.getProject().getName());
        projectDto.setStartDate(task.getProject().getStartDate());
        this.project = projectDto;
    }
}
