package com.skillconnect.server.repository;

import com.skillconnect.server.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, int> {
    // You can add custom query methods here if needed
}
