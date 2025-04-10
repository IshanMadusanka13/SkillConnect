package com.skillconnect.server.repository;

import com.skillconnect.server.model.Notification;
import com.skillconnect.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, int> {
    List<Notification> findByUser(User user);
    List<Notification> findByUserAndIsRead(User user, Boolean isRead);
}
