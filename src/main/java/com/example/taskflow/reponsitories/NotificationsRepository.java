package com.example.taskflow.reponsitories;

import com.example.taskflow.entities.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, Integer> {
    @Query("SELECT n FROM Notifications n WHERE n.user.id = :userId")
    public List<Notifications> findByUserId(@Param("userId") int userId);
}
