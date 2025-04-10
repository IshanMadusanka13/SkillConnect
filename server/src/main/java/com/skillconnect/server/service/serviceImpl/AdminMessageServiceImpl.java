package com.skillconnect.server.service.impl;

import com.skillconnect.server.model.AdminMessage;
import com.skillconnect.server.model.User;
import com.skillconnect.server.repository.AdminMessageRepository;
import com.skillconnect.server.repository.UserRepository;
import com.skillconnect.server.service.AdminMessageService;
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
public class AdminMessageServiceImpl implements AdminMessageService {

    private final AdminMessageRepository adminMessageRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public AdminMessageServiceImpl(
            AdminMessageRepository adminMessageRepository,
            UserRepository userRepository) {
        this.adminMessageRepository = adminMessageRepository;
        this.userRepository = userRepository;
        log.info("AdminMessageServiceImpl initialized");
    }
    
    @Override
    public AdminMessage createMessage(AdminMessage message, Long adminId) {
        log.info("Creating new admin message by admin ID: {}", adminId);
        
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> {
                    log.error("Admin user not found with ID: {}", adminId);
                    return new RuntimeException("Admin user not found with id: " + adminId);
                });
        
        // Verify the user is an admin (assuming there's a role field)
        if (!admin.getRole().equals("ADMIN")) {
            log.error("User with ID: {} is not an admin", adminId);
            throw new RuntimeException("User is not authorized to create admin messages");
        }
        
        message.setAdmin(admin);
        // The @PrePersist will handle setting createdAt
        
        AdminMessage savedMessage = adminMessageRepository.save(message);
        log.info("Admin message created successfully with ID: {}", savedMessage.getId());
        return savedMessage;
    }
    
    @Override
    public Optional<AdminMessage> findById(Long messageId) {
        log.debug("Finding admin message by ID: {}", messageId);
        return adminMessageRepository.findById(messageId);
    }
    
    @Override
    public List<AdminMessage> findAllMessages() {
        log.debug("Retrieving all admin messages");
        List<AdminMessage> messages = adminMessageRepository.findAll();
        log.debug("Found {} admin messages", messages.size());
        return messages;
    }
    
    @Override
    public List<AdminMessage> findMessagesByAdminId(Long adminId) {
        log.debug("Finding admin messages by admin ID: {}", adminId);
        List<AdminMessage> messages = adminMessageRepository.findByAdminId(adminId);
        log.debug("Found {} messages by admin ID: {}", messages.size(), adminId);
        return messages;
    }
    
    @Override
    public AdminMessage updateMessage(AdminMessage message) {
        log.info("Updating admin message with ID: {}", message.getId());
        if (!adminMessageRepository.existsById(message.getId())) {
            log.error("Admin message not found with ID: {}", message.getId());
            throw new RuntimeException("Admin message not found with id: " + message.getId());
        }
        
        AdminMessage updatedMessage = adminMessageRepository.save(message);
        log.info("Admin message updated successfully: {}", message.getId());
        return updatedMessage;
    }
    
    @Override
    public void deleteMessage(Long messageId) {
        log.info("Deleting admin message with ID: {}", messageId);
        adminMessageRepository.deleteById(messageId);
        log.info("Admin message deleted successfully: {}", messageId);
    }
    
    @Override
    public List<AdminMessage> findActiveMessages() {
        log.debug("Finding active admin messages");
        LocalDateTime now = LocalDateTime.now();
        List<AdminMessage> activeMessages = adminMessageRepository.findByActiveAndExpiryDateAfter(true, now);
        log.debug("Found {} active admin messages", activeMessages.size());
        return activeMessages;
    }
    
    @Override
    public AdminMessage setMessageActive(Long messageId, boolean active) {
        log.info("Setting admin message ID: {} active status to: {}", messageId, active);
        
        AdminMessage message = adminMessageRepository.findById(messageId)
                .orElseThrow(() -> {
                    log.error("Admin message not found with ID: {}", messageId);
                    return new RuntimeException("Admin message not found with id: " + messageId);
                });
        
        message.setActive(active);
        AdminMessage updatedMessage = adminMessageRepository.save(message);
        log.info("Admin message active status updated successfully: {}", messageId);
        return updatedMessage;
    }
    
    @Override
    public AdminMessage setMessageExpiryDate(Long messageId, LocalDateTime expiryDate) {
        log.info("Setting admin message ID: {} expiry date to: {}", messageId, expiryDate);
        
        AdminMessage message = adminMessageRepository.findById(messageId)
                .orElseThrow(() -> {
                    log.error("Admin message not found with ID: {}", messageId);
                    return new RuntimeException("Admin message not found with id: " + messageId);
                });
        
        message.setExpiryDate(expiryDate);
        AdminMessage updatedMessage = adminMessageRepository.save(message);
        log.info("Admin message expiry date updated successfully: {}", messageId);
        return updatedMessage;
    }
    
    @Override
    public List<AdminMessage> findMessagesByPriority(String priority) {
        log.debug("Finding admin messages by priority: {}", priority);
        List<AdminMessage> messages = adminMessageRepository.findByPriority(priority);
        log.debug("Found {} messages with priority: {}", messages.size(), priority);
        return messages;
    }
}
