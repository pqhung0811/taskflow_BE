package com.example.taskflow.reponsitories;

import com.example.taskflow.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId")
    public List<Task> findByProjectId(@Param("projectId") int projectId);
    @Query("SELECT t FROM Task t WHERE t.responsible.id = :userId")
    public List<Task> findByUserId(@Param("userId") int userId);
}
