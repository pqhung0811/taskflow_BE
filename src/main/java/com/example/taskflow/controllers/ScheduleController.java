package com.example.taskflow.controllers;

import com.example.taskflow.dtos.SchedulerTaskDto;
import com.example.taskflow.entities.EnumState;
import com.example.taskflow.entities.Task;
import com.example.taskflow.entities.User;
import com.example.taskflow.services.CustomUserDetails;
import com.example.taskflow.services.TaskService;
import com.example.taskflow.utils.JobSchedulingGeneticAlgorithm;
import com.example.taskflow.utils.SchedulerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ScheduleController {
    @Autowired
    private TaskService taskService;

    @GetMapping(path = "/tasks/schedule")
    public ResponseEntity<?> getScheduling() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();
            List<Task> tasks = taskService.getTasksByUserId(user.getId());
            List<Task> pendingTask = new ArrayList<>();
            for (Task task : tasks) {
                if (task.getState() == EnumState.ON_HOLD || task.getState() == EnumState.IN_PROGRESS) {
                    pendingTask.add(task);
                }
            }
            JobSchedulingGeneticAlgorithm scheduler = new JobSchedulingGeneticAlgorithm(pendingTask);
            List<SchedulerTask> bestSchedule = scheduler.geneticAlgorithm();
            List<SchedulerTaskDto> bestScheduleDtos = new ArrayList<>();
            for (SchedulerTask schedulerTask : bestSchedule) {
                SchedulerTaskDto schedulerTaskDto = new SchedulerTaskDto(schedulerTask);
                bestScheduleDtos.add(schedulerTaskDto);
            }
            Map<String, List<SchedulerTaskDto>> hasMap = new HashMap<>();
            hasMap.put("bestSchedule", bestScheduleDtos);
            return ResponseEntity.status(HttpStatus.OK).body(hasMap);
        }
    }
}
