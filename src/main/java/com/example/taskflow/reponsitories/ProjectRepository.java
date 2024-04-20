package com.example.taskflow.reponsitories;

import com.example.taskflow.entities.Project;
import com.example.taskflow.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query("SELECT p FROM Project p JOIN p.members m WHERE m.id = :userId")
    public List<Project> getProjectByUserId(@Param("userId") int userId);
    @Query("SELECT p.members FROM Project p WHERE p.id = :projectId")
    public List<User> getUserByProject(@Param("projectId") int projectId);
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO projectmember(project_id, user_id) VALUES (:projectId, :userId)", nativeQuery = true)
    public void addMemberToProject(@Param("projectId") int projectId, @Param("userId") int userId);
}
