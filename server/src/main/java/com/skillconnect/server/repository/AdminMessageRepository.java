package com.skillconnect.server.repository;

import com.skillconnect.server.model.AdminMessage;
import com.skillconnect.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminMessageRepository extends JpaRepository<AdminMessage, int> {
    List<AdminMessage> findByAdmin(User admin);
    List<AdminMessage> findByAdminOrderByCreatedAtDesc(User admin);
}
