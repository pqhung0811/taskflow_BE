package com.example.taskflow.dtos;

import com.example.taskflow.entities.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class TaskDto {
    private int id;
    private String title;
    private int advance;
    private EnumState state;
    private LocalDateTime startTime;
    private LocalDateTime deadline;
    private String description;
//    private List<Breakpoint> breakPoints = new ArrayList<Breakpoint>();
    private List<CommentDto> comments;
    private UserDto responsible;
    private ProjectDto project;

    public TaskDto(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.advance = task.getAdvance();
        this.state = task.getState();
        this.startTime = task.getStartTime();
        this.deadline = task.getDeadline();
        this.description = task.getDescription();
        List<CommentDto> commentDtos1 = new ArrayList<>();
        List<Comment> taskComments = task.getComments();
        for (Comment c : taskComments) {
            CommentDto commentDto = new CommentDto();
            commentDto.setId(c.getId());
            commentDto.setContent(c.getContent());
            commentDto.setupUser(c.getUser().getId(), c.getUser().getName(), c.getUser().getEmail());
            commentDto.setDate(c.getDate());
            commentDtos1.add(commentDto);
        }
        this.comments = commentDtos1;
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
