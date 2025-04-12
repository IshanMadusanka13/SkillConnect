package com.skillconnect.server.service;

import com.skillconnect.server.model.AdminMessage;

import java.util.List;
import java.util.Optional;

public interface AdminMessageService {
    
    AdminMessage createMessage(AdminMessage message);
    
    Optional<AdminMessage> findById(AdminMessage message);
    
    List<AdminMessage> findAllMessages();
    
    List<AdminMessage> findMessagesByAdminId(AdminMessage message);
    
    AdminMessage updateMessage(AdminMessage adminMessage);
    
    void deleteMessage(int messageId);
}
