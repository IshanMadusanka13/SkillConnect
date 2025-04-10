package com.skillconnect.server.repository;

import com.skillconnect.server.model.LearningPlanItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningPlanItemRepository extends JpaRepository<LearningPlanItem, int> {
    // You can add custom query methods here if needed
    // For example:
    // List<LearningPlanItem> findByLearningPlanId(int learningPlanId);
    // List<LearningPlanItem> findByLearningPlanIdOrderByOrderIndexAsc(int learningPlanId);
}
