package com.skillconnect.server.service;

import com.skillconnect.server.model.LearningPlan;
import com.skillconnect.server.model.User;

import java.util.List;
import java.util.Optional;

public interface LearningPlanService {
    
    LearningPlan saveLearningPlan(LearningPlan learningPlan);
    
    Optional<LearningPlan> findById(Long planId);
    
    List<LearningPlan> findLearningPlansByUser(User user);
    
    List<LearningPlan> findLearningPlansByUserId(Long userId);
    
    List<LearningPlan> findActiveLearningPlans(Long userId);
    
    LearningPlan updateLearningPlan(LearningPlan learningPlan);
    
    void deleteLearningPlan(Long planId);
    
    boolean isPlanOwner(Long planId, Long userId);
    
    void updatePlanStatus(Long planId, String status);
}
