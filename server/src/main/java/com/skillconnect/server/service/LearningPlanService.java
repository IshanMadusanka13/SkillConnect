package com.skillconnect.server.service;

import com.skillconnect.server.model.LearningPlan;
import com.skillconnect.server.model.LearningPlanItem;

import java.util.List;
import java.util.Optional;

public interface LearningPlanService {
    
    LearningPlan createLearningPlan(LearningPlan learningPlan);
    
    Optional<LearningPlan> findById(int planId);
    
    List<LearningPlan> findLearningPlansByUserId(int userId);
    
    LearningPlan updateLearningPlan(LearningPlan learningPlan);
    
    void deleteLearningPlan(int planId);

    void removeItemFromPlan(int itemId);

    LearningPlanItem addItemToPlan(LearningPlanItem item, int planId);
}
