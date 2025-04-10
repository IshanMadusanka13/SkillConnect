package com.skillconnect.server.service;

import com.skillconnect.server.model.Notification;
import com.skillconnect.server.model.User;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    
    Notification saveNotification(Notification notification);
    
    Notification createNotification(Long userId, String type, Long fromUserId, Long postId, Long commentId, String content);
    
    Optional<Notification> findById(Long notificationId);
    
    List<Notification> findNotificationsByUser(User user);
    
    List<Notification> findNotificationsByUserId(Long userId);
    
    List<Notification> findUnreadNotificationsByUser(User user);
    
    void markAsRead(Long notificationId);
    
    void markAllAsRead(Long userId);
    
    void deleteNotification(Long notificationId);
}
