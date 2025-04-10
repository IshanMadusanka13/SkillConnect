package com.skillconnect.server.repository;

import com.skillconnect.server.model.LearningUpdate;
import com.skillconnect.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LearningUpdateRepository extends JpaRepository<LearningUpdate, int> {
    List<LearningUpdate> findByUser(User user);
    List<LearningUpdate> findByUserOrderByCreatedAtDesc(User user);
}
