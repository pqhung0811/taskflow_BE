package com.example.taskflow.utils;

import com.example.taskflow.entities.Task;
import com.example.taskflow.utils.SchedulerTask;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class JobSchedulingGeneticAlgorithm {
    private static final int POPULATION_SIZE = 10; // Kích thước quần thể
    private static final int MAX_GENERATIONS = 30; // Số thế hệ tối đa
    private static final double MUTATION_RATE = 0.05; // Tỷ lệ đột biến
    private static final int WORK_HOURS_PER_DAY = 8;
    private static final int WORK_DAYS_PER_WEEK = 5;
    private static final LocalTime WORK_START_TIME = LocalTime.of(9, 0);
    private static final LocalTime WORK_LUNCH_START = LocalTime.of(12, 0);
    private static final LocalTime WORK_LUNCH_END = LocalTime.of(13, 0);
    private static final LocalTime WORK_END_TIME = LocalTime.of(18, 0);

    private List<Task> tasks;
    private Random random = new Random();

    public JobSchedulingGeneticAlgorithm(List<Task> tasks) {
        this.tasks = tasks;
    }

    private List<List<SchedulerTask>> initializePopulation() {
        List<List<SchedulerTask>> population = new ArrayList<>();
        // Khởi tạo quần thể bằng heuristic EDF kết hợp với ưu tiên
        population.addAll(initializeUsingEDF());
        // Thêm các giải pháp ngẫu nhiên vào quần thể
        while (population.size() < POPULATION_SIZE) {
            List<SchedulerTask> schedule = createRandomSchedule();
            population.add(schedule);
        }
        return population;
    }

    private List<List<SchedulerTask>> initializeUsingEDF() {
        List<List<SchedulerTask>> edfPopulation = new ArrayList<>();
        List<Task> sortedTasks = new ArrayList<>(tasks);
        sortedTasks.sort(Comparator.comparing((Task t) -> t.getDeadline())
                .thenComparing(t -> t.getPriority()));
        for (int i = 0; i < POPULATION_SIZE / 2; i++) {
            List<SchedulerTask> schedule = createEDFSchedule(sortedTasks);
            edfPopulation.add(schedule);
        }
        return edfPopulation;
    }

    private List<SchedulerTask> createEDFSchedule(List<Task> sortTasks) {
        List<SchedulerTask> schedule = new ArrayList<>();
        List<Task> sortedTasks = new ArrayList<>(sortTasks);
//        sortedTasks.sort(Comparator.comparing((Task t) -> t.getDeadline())
//                .thenComparing(t -> t.getPriority()));
        LocalDateTime currentTime = LocalDateTime.now().with(WORK_START_TIME);
        for (Task task : sortedTasks) {
            LocalDateTime startDoing = getNextAvailableTime(currentTime, task.getEstimateTime());
            LocalDateTime endDoing = calculateEndTime(startDoing, task.getEstimateTime());
            long delayInHours = Math.max(0, ChronoUnit.HOURS.between(task.getDeadline(), endDoing));
            schedule.add(new SchedulerTask(task, startDoing, endDoing, delayInHours));
            currentTime = endDoing;
        }
        return schedule;
    }

    private List<SchedulerTask> createRandomSchedule() {
        List<SchedulerTask> schedule = new ArrayList<>();
        List<Task> shuffledTasks = new ArrayList<>(tasks);
        Collections.shuffle(shuffledTasks);
        LocalDateTime currentTime = LocalDateTime.now().with(WORK_START_TIME); // Giả định bắt đầu làm việc lúc 9:00
        for (Task task : shuffledTasks) {
            LocalDateTime startDoing = getNextAvailableTime(currentTime, task.getEstimateTime());
            LocalDateTime endDoing = calculateEndTime(startDoing, task.getEstimateTime());
            long delayInHours = Math.max(0, ChronoUnit.HOURS.between(task.getDeadline(), endDoing));
            schedule.add(new SchedulerTask(task, startDoing, endDoing, delayInHours));
            currentTime = endDoing;
        }
        return schedule;
    }

    private LocalDateTime getNextAvailableTime(LocalDateTime currentTime, int estimateTime) {
        // Kiểm tra nếu thời gian hiện tại là giờ nghỉ trưa
        if (currentTime.toLocalTime().compareTo(WORK_LUNCH_START)==0) {
            currentTime = currentTime.with(WORK_LUNCH_END);
        }
        else if (currentTime.toLocalTime().compareTo(WORK_END_TIME)==0) {
            if(currentTime.getDayOfWeek() != DayOfWeek.FRIDAY) {
                currentTime = currentTime.with(WORK_START_TIME).plusDays(1);
            }
            else {
                currentTime = currentTime.with(WORK_START_TIME).plusDays(3);
            }
        }
        return currentTime;
    }

    private LocalDateTime calculateEndTime(LocalDateTime startTime, int estimateTime) {
        LocalDateTime endTime = startTime;
        long tmpMorningTime = 3;
        long tmpAfternoonTime = 5;
        long timeRemaining = estimateTime;

        while (timeRemaining>0) {
            if ((endTime.toLocalTime().isAfter(WORK_START_TIME)) &&
                    endTime.toLocalTime().isBefore(WORK_LUNCH_START)) {
                long tmpTimeHours = (long) Duration.between(endTime.toLocalTime(), WORK_LUNCH_START).toHours();
                if (timeRemaining>=tmpTimeHours) {
                    timeRemaining = timeRemaining - tmpTimeHours;
                    if(timeRemaining!=0) {
                        endTime = endTime.with(WORK_LUNCH_END);
                    }
                    else {
                        endTime = endTime.with(WORK_LUNCH_START);
                    }
                }
                else {
                    endTime = endTime.plusHours(timeRemaining);
                    timeRemaining = 0;
                }
            }
            else if ((endTime.toLocalTime().isAfter(WORK_LUNCH_END)) &&
                    endTime.toLocalTime().isBefore(WORK_END_TIME)) {
                long tmpTimeHours = (long) Duration.between(endTime.toLocalTime(), WORK_END_TIME).toHours();
                if (timeRemaining>=tmpTimeHours) {
                    timeRemaining = timeRemaining - tmpTimeHours;
                    if(timeRemaining!=0) {
                        if(endTime.getDayOfWeek() != DayOfWeek.FRIDAY) {
                            endTime = endTime.with(WORK_START_TIME).plusDays(1);
                        }
                        else if (endTime.getDayOfWeek() == DayOfWeek.FRIDAY) {
                            endTime = endTime.with(WORK_START_TIME).plusDays(3);
                        }
                    }
                    else {
                        endTime = endTime.with(WORK_END_TIME);
                    }
                }
                else {
                    endTime = endTime.plusHours(timeRemaining);
                    timeRemaining = 0;
                }
            }
            else if (endTime.toLocalTime().compareTo(WORK_START_TIME) == 0) {
                if (timeRemaining>=tmpMorningTime) {
                    timeRemaining = timeRemaining - tmpMorningTime;
                    if(timeRemaining!=0) {
                        endTime = endTime.with(WORK_LUNCH_END);
                    }
                    else {
                        endTime = endTime.with(WORK_LUNCH_START);
                    }
                }
                else {
                    endTime = endTime.plusHours(timeRemaining);
                    timeRemaining = 0;
                }
            }
            else if (endTime.toLocalTime().compareTo(WORK_LUNCH_END) == 0) {
                if (timeRemaining>=tmpAfternoonTime) {
                    timeRemaining = timeRemaining - tmpAfternoonTime;
                    if(timeRemaining!=0) {
                        if(endTime.getDayOfWeek() != DayOfWeek.FRIDAY) {
                            endTime = endTime.with(WORK_START_TIME).plusDays(1);
                        }
                        else if (endTime.getDayOfWeek() == DayOfWeek.FRIDAY) {
                            endTime = endTime.with(WORK_START_TIME).plusDays(3);
                        }
                    }
                    else {
                        endTime = endTime.with(WORK_END_TIME);
                    }
                }
                else {
                    endTime = endTime.plusHours(timeRemaining);
                    timeRemaining = 0;
                }
            }
        }
        return endTime;
    }

    private int calculateFitness(List<SchedulerTask> schedule) {
        int totalDelay = 0;
        int totalPriorityScore = 0;
        int positionTask = schedule.size();

        for (SchedulerTask schedulerTask : schedule) {
            totalDelay += schedulerTask.getDelayInHours();

            switch (schedulerTask.getTask().getPriority()) {
                case HIGH:
                    totalPriorityScore += 3*positionTask;
                    break;
                case MEDIUM:
                    totalPriorityScore += 2*positionTask;
                    break;
                case LOW:
                    totalPriorityScore += 1*positionTask;
                    break;
            }
            positionTask--;
        }

        return totalDelay * 10 - totalPriorityScore; // Hệ số 10 để ưu tiên giảm delay trước
    }

    private List<SchedulerTask> selection(List<List<SchedulerTask>> population) {
        int tournamentSize = 2;
        List<SchedulerTask> bestSchedule = null;
        int bestFitness = Integer.MAX_VALUE;
        for (int i = 0; i < tournamentSize; i++) {
            List<SchedulerTask> candidate = population.get(random.nextInt(POPULATION_SIZE));
            int fitness = calculateFitness(candidate);
            if (fitness < bestFitness) {
                bestFitness = fitness;
                bestSchedule = candidate;
            }
        }
        return bestSchedule;
    }

    private List<SchedulerTask> crossover(List<SchedulerTask> parent1, List<SchedulerTask> parent2) {
        int crossoverPoint = random.nextInt(tasks.size());
        List<SchedulerTask> child = new ArrayList<>(parent1.subList(0, crossoverPoint));
        Set<Integer> taskIds = new HashSet<>(child.stream().map(st -> st.getTask().getId()).toList());
        for (SchedulerTask schedulerTask : parent2) {
            if (!taskIds.contains(schedulerTask.getTask().getId())) {
                child.add(schedulerTask);
            }
        }
        return recalculateScheduleTimes(child);
    }

    private List<SchedulerTask> mutate(List<SchedulerTask> schedule) {
        int idx1 = random.nextInt(schedule.size());
        int idx2 = random.nextInt(schedule.size());
        Collections.swap(schedule, idx1, idx2);
        return recalculateScheduleTimes(schedule);
    }

    private void replace(List<List<SchedulerTask>> population, List<SchedulerTask> newSchedule) {
        int worstFitness = Integer.MIN_VALUE;
        int worstIndex = 0;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            int fitness = calculateFitness(population.get(i));
            if (fitness > worstFitness) {
                worstFitness = fitness;
                worstIndex = i;
            }
        }
        population.set(worstIndex, newSchedule);
    }

    private List<SchedulerTask> recalculateScheduleTimes(List<SchedulerTask> schedule) {
        LocalDateTime currentTime = LocalDateTime.now().with(WORK_START_TIME);
        for (SchedulerTask schedulerTask : schedule) {
            currentTime = getNextAvailableTime(currentTime, schedulerTask.getTask().getEstimateTime());
            LocalDateTime endDoing = calculateEndTime(currentTime, schedulerTask.getTask().getEstimateTime());
            schedulerTask.setStartDoing(currentTime);
            schedulerTask.setEndDoing(endDoing);
            schedulerTask.setDelayInHours(Math.max(0, ChronoUnit.HOURS.between(schedulerTask.getTask().getDeadline(), endDoing)));
            currentTime = endDoing;
        }
        return schedule;
    }

    private List<SchedulerTask> getBestSchedule(List<List<SchedulerTask>> population) {
        int fitposi=0, posi=0;
        List<SchedulerTask> bestSchedule = population.get(0); // Giả sử lịch trình đầu tiên là tốt nhất ban đầu
        int bestFitness = calculateFitness(bestSchedule);
        System.out.println(bestFitness);

        for (List<SchedulerTask> schedule : population) {
            int fitness = calculateFitness(schedule);
            System.out.println("fitness " + fitness);
            if (fitness < bestFitness) {
                System.out.println(bestFitness);
                fitposi=posi;
                bestFitness = fitness;
                bestSchedule = schedule;
                System.out.println("fitposi" + fitposi);
            }
            posi++;

        }
        return bestSchedule;
    }

    public List<SchedulerTask> geneticAlgorithm() {
        List<List<SchedulerTask>> population = initializePopulation();

        for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
            // Perform selection, crossover, mutation, and replacement
            List<List<SchedulerTask>> newPopulation = new ArrayList<>();
            for (int i = 0; i < POPULATION_SIZE / 2; i++) {
                List<SchedulerTask> parent1 = selection(population);
                List<SchedulerTask> parent2 = selection(population);
                List<SchedulerTask> child = crossover(parent1, parent2);

                if (random.nextDouble() < MUTATION_RATE) {
                    child = mutate(child);
                }

                newPopulation.add(child);
            }

            // Replace the worst schedules in the current population with the new schedules
            for (int i = 0; i < POPULATION_SIZE / 2; i++) {
                replace(population, newPopulation.get(i));
            }
        }
        return getBestSchedule(population);
    }
}
