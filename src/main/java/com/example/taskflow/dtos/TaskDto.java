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
    private List<CommentDto> comments;
    private UserDto responsible;
    private ProjectDto project;
    private List<FileAttachmentDto> files;
    private EnumPriority priority;
    private EnumCategory category;
    private int estimateTime;

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
        if (task.getResponsible()!=null) {
            UserDto userDto = new UserDto();
            userDto.setId(task.getResponsible().getId());
            userDto.setEmail(task.getResponsible().getEmail());
            userDto.setName(task.getResponsible().getName());
            this.responsible = userDto;
        }
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(task.getProject().getId());
        projectDto.setName(task.getProject().getName());
        projectDto.setStartDate(task.getProject().getStartDate());
        this.project = projectDto;
        List<FileAttachmentDto> fileAttachmentDtos = new ArrayList<>();
        List<FileAttachment> fileAttachments1 = task.getFileAttachments();
        for (FileAttachment f : fileAttachments1) {
            FileAttachmentDto fileAttachmentDto = new FileAttachmentDto();
            fileAttachmentDto.setId(f.getId());
            fileAttachmentDto.setFilename(f.getFileName());
            fileAttachmentDtos.add(fileAttachmentDto);
        }
        this.files = fileAttachmentDtos;
        this.priority = task.getPriority();
        this.category = task.getCategory();
        this.estimateTime = task.getEstimateTime();
    }
}
