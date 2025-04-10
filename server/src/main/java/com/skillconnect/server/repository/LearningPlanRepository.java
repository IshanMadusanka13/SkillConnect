package com.skillconnect.server.repository;

import com.skillconnect.server.model.LearningPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningPlanRepository extends JpaRepository<LearningPlan, int> {
    // You can add custom query methods here if needed
    // For example:
    // List<LearningPlan> findByStatus(String status);
    // List<LearningPlan> findByPostId(int postId);
}
