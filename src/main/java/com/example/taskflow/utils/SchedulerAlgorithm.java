//package com.example.taskflow.utils;
//
//import com.example.taskflow.entities.EnumState;
//import com.example.taskflow.entities.Task;
//import org.springframework.scheduling.config.ScheduledTask;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//public class SchedulerAlgorithm {
//    public List<Task> getPendingTask(List<Task> tasks) {
//        List<Task> pendingTasks = new ArrayList<>();
//        for (Task task : tasks) {
//            if (task.getState() == EnumState.ON_HOLD || task.getState() == EnumState.IN_PROGRESS) {
//                pendingTasks.add(task);
//            }
//        }
//        return pendingTasks;
//    }
//    public List<SchedulerTask> schedulerPriority(List<Task> pendingTasks) {
////        Collections.sort(pendingTasks, new TaskComparato());
//        pendingTasks.sort(Comparator.comparing(Task::getDeadline)
//                .thenComparing((Task t) -> {
//                    switch (t.getPriority()) {
//                        case HIGH: return 1;
//                        case MEDIUM: return 2;
//                        case LOW: return 3;
//                        default: return 4;
//                    }
//                }));
//
//        List<ScheduledTask> scheduledTasks = new ArrayList<>();
//        LocalDateTime currentTime = LocalDateTime.now();
//        LocalDateTime endOfWorkDay = currentTime.withHour(17).withMinute(0); // Giả sử ngày làm việc kết thúc lúc 17:00
//        int hoursPerDay = 8;
//
//        for (Task task : pendingTasks) {
//            LocalDateTime startDoing = currentTime;
//            LocalDateTime endDoing = startDoing.plusHours(task.getEstimateHourWork());
//
//            // Nếu task không thể hoàn thành trong ngày hiện tại, chuyển sang ngày hôm sau
//            if (endDoing.isAfter(endOfWorkDay)) {
//                startDoing = currentTime.withHour(9).withMinute(0).plusDays(1);
//                endDoing = startDoing.plusHours(task.getEstimateHourWork());
//                currentTime = endDoing;
//            } else {
//                currentTime = endDoing;
//            }
//
//            // Tính toán thời gian trễ nếu có
//            long delayInHours = 0;
//            if (endDoing.isAfter(task.getDeadline())) {
//                delayInHours = Duration.between(task.getDeadline(), endDoing).toHours();
//            }
//
//            scheduledTasks.add(new SchedulerTask(task, startDoing, endDoing, delayInHours));
//        }
//    }
//}
