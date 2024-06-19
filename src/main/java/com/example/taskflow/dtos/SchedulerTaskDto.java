package com.example.taskflow.dtos;

import com.example.taskflow.entities.Task;
import com.example.taskflow.utils.SchedulerTask;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SchedulerTaskDto {
    private int taskId;
    private String taskName;
    private int projectId;
    private String projectName;
    private LocalDateTime startDoing;
    private LocalDateTime endDoing;
    private long delayInHours;

    public SchedulerTaskDto(SchedulerTask schedulerTask) {
        Task task = schedulerTask.getTask();
        this.taskId = task.getId();
        this.taskName = task.getTitle();
        this.projectId = task.getProject().getId();
        this.projectName = task.getProject().getName();
        this.startDoing = schedulerTask.getStartDoing();
        this.endDoing = schedulerTask.getEndDoing();
        this.delayInHours = schedulerTask.getDelayInHours();
    }
}
