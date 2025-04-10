package com.skillconnect.server.service;

import com.skillconnect.server.model.LearningPlanItem;
import com.skillconnect.server.model.LearningPlan;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LearningPlanItemService {
    
    LearningPlanItem saveLearningPlanItem(LearningPlanItem learningPlanItem);
    Optional<LearningPlanItem> findById(Long itemId);
    List<LearningPlanItem> findByLearningPlan(LearningPlan learningPlan);
    List<LearningPlanItem> findByLearningPlanId(Long planId);
    List<LearningPlanItem> findByLearningPlanIdOrderByOrderIndexAsc(Long planId);
    LearningPlanItem updateLearningPlanItem(LearningPlanItem learningPlanItem);
    void deleteLearningPlanItem(Long itemId);
    LearningPlanItem updateItemStatus(Long itemId, String status);
    LearningPlanItem markItemAsCompleted(Long itemId, LocalDate completionDate);
    LearningPlanItem reorderItem(Long itemId, Integer newOrderIndex);
    LearningPlanItem addResource(Long itemId, String resource);
    LearningPlanItem removeResource(Long itemId, String resource);
    boolean isItemOwner(Long itemId, Long userId);
}
