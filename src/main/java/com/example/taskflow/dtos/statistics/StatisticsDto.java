package com.example.taskflow.dtos.statistics;

import com.example.taskflow.entities.EnumCategory;
import com.example.taskflow.entities.EnumPriority;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class StatisticsDto {
    private Map<EnumPriority, Integer> priority;
    private Map<EnumCategory, Integer> category;
    private Map<String, Integer> deadline;
    private Map<Integer, Double> weeklyAverageCompletionTimes;
    private Map<Integer, Integer> completedTasksPerWeek;
    private Map<Integer, Integer> receivedTasksPerWeek;


    public StatisticsDto() {
        priority = new HashMap<>();
        category = new HashMap<>();
        deadline = new HashMap<>();
    }

    public void addPriority(EnumPriority enumPriority) {
        if (enumPriority == null) {
            return; // Bỏ qua nếu khóa là null
        }
        this.priority.merge(enumPriority, 1, Integer::sum);
    }

    public void addCategory(EnumCategory enumCategory) {
        if (enumCategory == null) {
            return; // Bỏ qua nếu khóa là null
        }
        this.category.merge(enumCategory, 1, Integer::sum);
    }

    public void addDeadline(String type) {
        if (type == null) {
            return; // Bỏ qua nếu khóa là null
        }
        this.deadline.merge(type, 1, Integer::sum);
    }

}
