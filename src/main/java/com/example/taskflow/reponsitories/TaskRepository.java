package com.example.taskflow.reponsitories;

import com.example.taskflow.entities.*;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId")
    public List<Task> findByProjectId(@Param("projectId") int projectId);
    @Query("SELECT DISTINCT t FROM Task t LEFT JOIN FETCH t.comments c WHERE t.responsible.id = :userId")
    public List<Task> findByUserId(@Param("userId") int userId);
    @Transactional
    @Modifying
    @Query("UPDATE Task t SET t.title = :title WHERE t.id = :taskId")
    public int updateTitleById(int taskId, String title);
    @Transactional
    @Modifying
    @Query("UPDATE Task t SET t.description = :description WHERE t.id = :taskId")
    public int updateDescriptionById(int taskId, String description);
    @Transactional
    @Modifying
    @Query("UPDATE Task t SET t.advance = :advance WHERE t.id = :taskId")
    public int updateAdvanceById(int taskId, int advance);
    @Transactional
    @Modifying
    @Query("UPDATE Task t SET t.deadline = :deadline WHERE t.id = :taskId")
    public int updateDeadlineById(int taskId, LocalDateTime deadline);
    @Transactional
    @Modifying
    @Query("UPDATE Task t SET t.state = :newState WHERE t.id = :taskId")
    public int updateStateById(int taskId, EnumState newState);
    @Query("SELECT c FROM Comment c WHERE c.task.id = :taskId")
    public List<Comment> findCommentByTaskId(int taskId);
    @Query("SELECT f FROM FileAttachment f WHERE f.task.id = :taskId")
    public List<FileAttachment> findFileByTaskId(int taskId);
    @Transactional
    @Modifying
    @Query("UPDATE Task t SET t.priority = :newPriority WHERE t.id = :taskId")
    public int updatePriorityById(int taskId, EnumPriority newPriority);
    @Transactional
    @Modifying
    @Query("UPDATE Task t SET t.state = :newState, t.endTime = :newEndTime WHERE t.id = :taskId")
    public int updateEndTimeById(int taskId, EnumState newState, LocalDateTime newEndTime);
    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.responsible = NULL WHERE t.responsible.id = :userId AND t.project.id = :projectId")
    public void setResponsibleToNullByUserId(int userId, int projectId);
    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.responsible.id = :userId WHERE t.id = :taskId")
    public void updateResponsible(int userId, int taskId);
}
