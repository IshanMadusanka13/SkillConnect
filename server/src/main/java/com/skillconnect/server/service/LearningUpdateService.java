package com.skillconnect.server.service;

import com.skillconnect.server.model.LearningUpdate;
import com.skillconnect.server.model.User;

import java.util.List;
import java.util.Optional;

public interface LearningUpdateService {
    
    LearningUpdate saveLearningUpdate(LearningUpdate learningUpdate);
    
    Optional<LearningUpdate> findById(Long updateId);
    
    List<LearningUpdate> findLearningUpdatesByUser(User user);
    
    List<LearningUpdate> findLearningUpdatesByUserId(Long userId);
    
    LearningUpdate updateLearningUpdate(LearningUpdate learningUpdate);
    
    void deleteLearningUpdate(Long updateId);
    
    boolean isUpdateOwner(Long updateId, Long userId);
}
