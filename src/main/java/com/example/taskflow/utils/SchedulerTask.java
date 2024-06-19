package com.example.taskflow.utils;

import com.example.taskflow.entities.Task;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SchedulerTask {
    private Task task;
    private LocalDateTime startDoing;
    private LocalDateTime endDoing;
    private long delayInHours;

    public SchedulerTask(Task task, LocalDateTime startDoing, LocalDateTime endDoing, long delayInHours) {
        this.task = task;
        this.startDoing = startDoing;
        this.endDoing = endDoing;
        this.delayInHours = delayInHours;
    }
}
