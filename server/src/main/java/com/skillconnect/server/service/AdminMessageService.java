package com.skillconnect.server.service;

import com.skillconnect.server.model.AdminMessage;
import com.skillconnect.server.model.User;

import java.util.List;
import java.util.Optional;

public interface AdminMessageService {
    
    AdminMessage saveAdminMessage(AdminMessage adminMessage);
    
    Optional<AdminMessage> findById(Long messageId);
    
    List<AdminMessage> findAllAdminMessages();
    
    List<AdminMessage> findAdminMessagesByAdmin(User admin);
    
    List<AdminMessage> findAdminMessagesByAdminId(Long adminId);
    
    AdminMessage updateAdminMessage(AdminMessage adminMessage);
    
    void deleteAdminMessage(Long messageId);
    
    boolean isMessageOwner(Long messageId, Long adminId);
}
