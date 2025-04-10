package com.skillconnect.server.service.impl;

import com.skillconnect.server.model.Notification;
import com.skillconnect.server.model.User;
import com.skillconnect.server.repository.NotificationRepository;
import com.skillconnect.server.repository.UserRepository;
import com.skillconnect.server.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        log.info("NotificationServiceImpl initialized");
    }
    
    @Override
    public Notification createNotification(Notification notification, Long userId) {
        log.info("Creating notification for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new RuntimeException("User not found with id: " + userId);
                });
        
        notification.setUser(user);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        
        Notification savedNotification = notificationRepository.save(notification);
        log.info("Notification created successfully with ID: {}", savedNotification.getId());
        return savedNotification;
    }
    
    @Override
    public Optional<Notification> findById(Long notificationId) {
        log.debug("Finding notification by ID: {}", notificationId);
        return notificationRepository.findById(notificationId);
    }
    
    @Override
    public List<Notification> findNotificationsByUserId(Long userId) {
        log.debug("Finding notifications for user ID: {}", userId);
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        log.debug("Found {} notifications for user ID: {}", notifications.size(), userId);
        return notifications;
    }
    
    @Override
    public List<Notification> findUnreadNotificationsByUserId(Long userId) {
        log.debug("Finding unread notifications for user ID: {}", userId);
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndReadFalse(userId);
        log.debug("Found {} unread notifications for user ID: {}", unreadNotifications.size(), userId);
        return unreadNotifications;
    }
    
    @Override
    public Notification markAsRead(Long notificationId) {
        log.info("Marking notification ID: {} as read", notificationId);
        
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> {
                    log.error("Notification not found with ID: {}", notificationId);
                    return new RuntimeException("Notification not found with id: " + notificationId);
                });
        
        notification.setRead(true);
        notification.setReadAt(LocalDateTime.now());
        
        Notification updatedNotification = notificationRepository.save(notification);
        log.info("Notification marked as read: {}", notificationId);
        return updatedNotification;
    }
    
    @Override
    public void markAllAsRead(Long userId) {
        log.info("Marking all notifications as read for user ID: {}", userId);
        
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndReadFalse(userId);
        LocalDateTime now = LocalDateTime.now();
        
        for (Notification notification : unreadNotifications) {
            notification.setRead(true);
            notification.setReadAt(now);
        }
        
        notificationRepository.saveAll(unreadNotifications);
        log.info("Marked {} notifications as read for user ID: {}", unreadNotifications.size(), userId);
    }
    
    @Override
    public void deleteNotification(Long notificationId) {
        log.info("Deleting notification with ID: {}", notificationId);
        notificationRepository.deleteById(notificationId);
        log.info("Notification deleted successfully: {}", notificationId);
    }
    
    @Override
    public void deleteAllNotifications(Long userId) {
        log.info("Deleting all notifications for user ID: {}", userId);
        notificationRepository.deleteByUserId(userId);
        log.info("All notifications deleted for user ID: {}", userId);
    }
    
    @Override
    public int countUnreadNotifications(Long userId) {
        log.debug("Counting unread notifications for user ID: {}", userId);
        int count = notificationRepository.countByUserIdAndReadFalse(userId);
        log.debug("User ID: {} has {} unread notifications", userId, count);
        return count;
    }
    
    @Override
    public List<Notification> findRecentNotifications(Long userId, int limit) {
        log.debug("Finding recent {} notifications for user ID: {}", limit, userId);
        List<Notification> recentNotifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, limit);
        log.debug("Found {} recent notifications for user ID: {}", recentNotifications.size(), userId);
        return recentNotifications;
    }
    
    @Override
    public Notification createSystemNotification(String message, String type, Long userId) {
        log.info("Creating system notification for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new RuntimeException("User not found with id: " + userId);
                });
        
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        notification.setSystemGenerated(true);
        
        Notification savedNotification = notificationRepository.save(notification);
        log.info("System notification created successfully with ID: {}", savedNotification.getId());
        return savedNotification;
    }
    
    @Override
    public List<Notification> findNotificationsByType(Long userId, String type) {
        log.debug("Finding notifications of type: {} for user ID: {}", type, userId);
        List<Notification> notifications = notificationRepository.findByUserIdAndType(userId, type);
        log.debug("Found {} notifications of type: {} for user ID: {}", notifications.size(), type, userId);
        return notifications;
    }
}
