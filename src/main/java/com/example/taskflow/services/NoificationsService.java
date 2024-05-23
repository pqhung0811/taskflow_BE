package com.example.taskflow.services;

import com.example.taskflow.entities.Notifications;
import com.example.taskflow.reponsitories.NotificationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoificationsService {
    @Autowired
    private NotificationsRepository notificationsRepository;

    public List<Notifications> getNotificationsByUserId(int userId) {
        return notificationsRepository.findByUserId(userId);
    }
}
