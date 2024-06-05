package com.example.taskflow.controllers;

import com.example.taskflow.dtos.statistics.StatisticsDto;
import com.example.taskflow.entities.EnumPriority;
import com.example.taskflow.entities.EnumState;
import com.example.taskflow.entities.Task;
import com.example.taskflow.services.CustomUserDetails;
import com.example.taskflow.services.TaskService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class StatisticsController {
    @Autowired
    private TaskService taskService;

    @GetMapping(path = "/statistics/{projectId}")
    public ResponseEntity<?> getStatisticsCategory(@PathVariable int projectId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, StatisticsDto> statisticsDtoMap = new HashMap<>();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            List<Task> tasks = taskService.getTasksByProjectId(projectId);
            Map<String, Map<Integer, List<Long>>> weeklyCompletionTimesMap = new HashMap<>();
            Map<String, Map<Integer, Integer>> completedTasksPerWeekMap = new HashMap<>();
            Map<String, Map<Integer, Integer>> receivedTasksPerWeekMap = new HashMap<>();
            LocalDateTime currentDateTime = LocalDateTime.now();
            List<Integer> recentWeeks = getRecentWeeks();
            for (Task t : tasks) {
                initMapping(t, statisticsDtoMap, weeklyCompletionTimesMap,
                        completedTasksPerWeekMap, receivedTasksPerWeekMap);
                StatisticsDto statisticsDto = statisticsDtoMap.get(t.getResponsible().getEmail());
                Map<Integer, List<Long>> weeklyCompletionTimes = weeklyCompletionTimesMap.get(t.getResponsible().getEmail());
                Map<Integer, Integer> completedTasksPerWeek = completedTasksPerWeekMap.get(t.getResponsible().getEmail());
                Map<Integer, Integer> receivedTasksPerWeek = receivedTasksPerWeekMap.get(t.getResponsible().getEmail());
                statisticsDto.addPriority(t.getPriority());
                statisticsDto.addCategory(t.getCategory());
                LocalDateTime deadline = t.getDeadline();
                LocalDateTime endTime = t.getEndTime();
                if (endTime != null) {
                    if (endTime.isAfter(deadline)) {
                        statisticsDto.addDeadline("OutOfDate");
                    }
                    else {
                        statisticsDto.addDeadline("Completed");
                    }
                }
                else {
                    if (currentDateTime.isBefore(deadline)) {
                        statisticsDto.addDeadline("Processing");
                    }
                    else {
                        statisticsDto.addDeadline("ProcessingAndOutOfDate");
                    }
                }
                calculateWeekly(t, recentWeeks, weeklyCompletionTimes,
                        completedTasksPerWeek, receivedTasksPerWeek);
            }
            for (Map.Entry<String, StatisticsDto> entry : statisticsDtoMap.entrySet()) {
                String email = entry.getKey();
                StatisticsDto statisticsDto = entry.getValue();
                Map<Integer, List<Long>> weeklyCompletionTimes = weeklyCompletionTimesMap.get(email);
                Map<Integer, Integer> completedTasksPerWeek = completedTasksPerWeekMap.get(email);
                Map<Integer, Integer> receivedTasksPerWeek = receivedTasksPerWeekMap.get(email);
                Map<Integer, Double> weeklyAverageCompletionTimes = calculateTime(weeklyCompletionTimes);
                statisticsDto.setWeeklyAverageCompletionTimes(weeklyAverageCompletionTimes);
                statisticsDto.setCompletedTasksPerWeek(completedTasksPerWeek);
                statisticsDto.setReceivedTasksPerWeek(receivedTasksPerWeek);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(statisticsDtoMap);
    }

    public void initMapping(Task t, Map<String, StatisticsDto> statisticsDtoMap,
                            Map<String, Map<Integer, List<Long>>> weeklyCompletionTimesMap,
                            Map<String, Map<Integer, Integer>> completedTasksPerWeekMap,
                            Map<String, Map<Integer, Integer>> receivedTasksPerWeekMap)
    {
        if (!statisticsDtoMap.containsKey(t.getResponsible().getEmail())) {
            StatisticsDto statisticsDto = new StatisticsDto();
            statisticsDtoMap.put(t.getResponsible().getEmail(), statisticsDto);
        }
        if (!weeklyCompletionTimesMap.containsKey(t.getResponsible().getEmail())) {
            Map<Integer, List<Long>> weeklyCompletionTimes = new HashMap<>();
            weeklyCompletionTimesMap.put(t.getResponsible().getEmail(), weeklyCompletionTimes);
        }
        if (!completedTasksPerWeekMap.containsKey(t.getResponsible().getEmail())) {
            Map<Integer, Integer> completedTasksPerWeek = new HashMap<>();
            completedTasksPerWeekMap.put(t.getResponsible().getEmail(), completedTasksPerWeek);
        }
        if (!receivedTasksPerWeekMap.containsKey(t.getResponsible().getEmail())) {
            Map<Integer, Integer> receivedTasksPerWeek = new HashMap<>();
            receivedTasksPerWeekMap.put(t.getResponsible().getEmail(), receivedTasksPerWeek);
        }
    }

    @GetMapping(path = "/statistics")
    public ResponseEntity<?> getStatisticsCategory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        StatisticsDto statisticsDto;
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            Map<Integer, List<Long>> weeklyCompletionTimes = new HashMap<>();
            Map<Integer, Integer> completedTasksPerWeek = new HashMap<>();
            Map<Integer, Integer> receivedTasksPerWeek = new HashMap<>();
            List<Integer> recentWeeks = getRecentWeeks();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            statisticsDto = new StatisticsDto();
            LocalDateTime currentDateTime = LocalDateTime.now();
            List<Task> tasks = taskService.getTasksByUserId(userDetails.getUser().getId());
            for (Task t : tasks) {
                statisticsDto.addPriority(t.getPriority());
                statisticsDto.addCategory(t.getCategory());
                LocalDateTime deadline = t.getDeadline();
                LocalDateTime endTime = t.getEndTime();
                if (endTime != null) {
                    if (endTime.isAfter(deadline)) {
                        statisticsDto.addDeadline("OutOfDate");
                    }
                    else {
                        statisticsDto.addDeadline("Completed");
                    }
                }
                else {
                    if (currentDateTime.isBefore(deadline)) {
                        statisticsDto.addDeadline("Processing");
                    }
                    else {
                        statisticsDto.addDeadline("ProcessingAndOutOfDate");
                    }
                }
                calculateWeekly(t, recentWeeks, weeklyCompletionTimes,
                                    completedTasksPerWeek, receivedTasksPerWeek);
            }
            Map<Integer, Double> weeklyAverageCompletionTimes = calculateTime(weeklyCompletionTimes);
            statisticsDto.setWeeklyAverageCompletionTimes(weeklyAverageCompletionTimes);
            statisticsDto.setCompletedTasksPerWeek(completedTasksPerWeek);
            statisticsDto.setReceivedTasksPerWeek(receivedTasksPerWeek);
        }
        Map<String, StatisticsDto> statisticsDtoMap = new HashMap<>();
        statisticsDtoMap.put("statistics", statisticsDto);
        return ResponseEntity.status(HttpStatus.OK).body(statisticsDtoMap);
    }

    public void calculateWeekly(Task task, List<Integer> recentWeeks,
                                Map<Integer, List<Long>> weeklyCompletionTimes,
                                Map<Integer, Integer> completedTasksPerWeek,
                                Map<Integer, Integer> receivedTasksPerWeek) {
        LocalDateTime endTime = task.getEndTime();
        LocalDateTime startTime = task.getStartTime();
        if (endTime != null) {
            int weekNumber = endTime.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            if (recentWeeks.contains(weekNumber)) {
                completedTasksPerWeek.merge(weekNumber, 1, Integer::sum);
                long completionTime = Duration.between(startTime, endTime).toHours();
                weeklyCompletionTimes.computeIfAbsent(weekNumber, k -> new ArrayList<>()).add(completionTime);
            }
        }
        int weekNumber = startTime.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        if (recentWeeks.contains(weekNumber)) {
            receivedTasksPerWeek.merge(weekNumber, 1, Integer::sum);
        }
    }

    public Map<Integer, Double> calculateTime(Map<Integer, List<Long>> weeklyCompletionTimes) {
        Map<Integer, Double> weeklyAverageCompletionTimes = new HashMap<>();
        for (Map.Entry<Integer, List<Long>> entry : weeklyCompletionTimes.entrySet()) {
            List<Long> completionTimes = entry.getValue();
            double average = completionTimes.stream().mapToLong(val -> val).average().orElse(0.0);
            weeklyAverageCompletionTimes.put(entry.getKey(), average);
        }
        return weeklyAverageCompletionTimes.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, HashMap::new));
    }

    public List<Integer> getRecentWeeks() {
        LocalDateTime now = LocalDateTime.now();
        int currentWeek = now.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        int currentYear = now.getYear();
        List<Integer> recentWeeks = new ArrayList<>();
        int weekCount = 25;
        int currentWeekCopy = currentWeek;
        for (int i = 0; i < weekCount; i++) {
            recentWeeks.add(currentWeekCopy);
            if (currentWeekCopy == 1) {
                currentYear--;
                currentWeekCopy = LocalDateTime.of(currentYear, 12, 31, 0, 0).get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            } else {
                currentWeekCopy--;
            }
        }
        return recentWeeks;
    }
}
