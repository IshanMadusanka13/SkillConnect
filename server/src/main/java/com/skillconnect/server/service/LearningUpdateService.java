package com.skillconnect.server.service;

import com.skillconnect.server.model.LearningPlan;
import com.skillconnect.server.model.LearningUpdate;
import com.skillconnect.server.model.UpdateTemplate;

import java.util.List;
import java.util.Optional;

public interface LearningUpdateService {

    LearningUpdate createUpdateFromTemplate(UpdateTemplate template, LearningPlan plan);

    LearningUpdate createUpdate(LearningUpdate update, int planId);
    
    Optional<LearningUpdate> findById(int updateId);
    
    List<LearningUpdate> findUpdatesByUserId(int userId);
    
    LearningUpdate updateLearningUpdate(LearningUpdate learningUpdate);
    
    void deleteUpdate(int updateId);
}
