package com.example.taskflow.reponsitories;

import com.example.taskflow.entities.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, Integer> {
    @Query("SELECT n FROM Notifications n WHERE n.user.id = :userId")
    public List<Notifications> findByUserId(@Param("userId") int userId);
    @Modifying
    @Transactional
    @Query("UPDATE Notifications n SET n.isRead = true WHERE n.user.id = :userId")
    void markAllAsReadByUserId(int userId);
}
