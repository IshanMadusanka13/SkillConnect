package com.skillconnect.server.service.impl;

import com.skillconnect.server.model.LearningPlan;
import com.skillconnect.server.model.LearningPlanItem;
import com.skillconnect.server.model.User;
import com.skillconnect.server.repository.LearningPlanRepository;
import com.skillconnect.server.repository.LearningPlanItemRepository;
import com.skillconnect.server.repository.UserRepository;
import com.skillconnect.server.service.LearningPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@Transactional
public class LearningPlanServiceImpl implements LearningPlanService {

    private final LearningPlanRepository learningPlanRepository;
    private final LearningPlanItemRepository learningPlanItemRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public LearningPlanServiceImpl(
            LearningPlanRepository learningPlanRepository,
            LearningPlanItemRepository learningPlanItemRepository,
            UserRepository userRepository) {
        this.learningPlanRepository = learningPlanRepository;
        this.learningPlanItemRepository = learningPlanItemRepository;
        this.userRepository = userRepository;
        log.info("LearningPlanServiceImpl initialized");
    }
    
    @Override
    public LearningPlan createLearningPlan(LearningPlan learningPlan, Long userId) {
        log.info("Creating new learning plan for user ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new RuntimeException("User not found with id: " + userId);
                });
        
        learningPlan.setUser(user);
        // Note: The @PrePersist will handle setting createdAt and updatedAt
        
        LearningPlan savedPlan = learningPlanRepository.save(learningPlan);
        log.info("Learning plan created successfully with ID: {}", savedPlan.getId());
        return savedPlan;
    }
    
    @Override
    public Optional<LearningPlan> findById(Long planId) {
        log.debug("Finding learning plan by ID: {}", planId);
        return learningPlanRepository.findById(planId);
    }
    
    @Override
    public List<LearningPlan> findLearningPlansByUserId(Long userId) {
        log.debug("Finding learning plans for user ID: {}", userId);
        List<LearningPlan> plans = learningPlanRepository.findByUserId(userId);
        log.debug("Found {} learning plans for user ID: {}", plans.size(), userId);
        return plans;
    }
    
    @Override
    public LearningPlan updateLearningPlan(LearningPlan learningPlan) {
        log.info("Updating learning plan with ID: {}", learningPlan.getId());
        if (!learningPlanRepository.existsById(learningPlan.getId())) {
            log.error("Learning plan not found with ID: {}", learningPlan.getId());
            throw new RuntimeException("Learning plan not found with id: " + learningPlan.getId());
        }
        
        learningPlan.setUpdatedAt(LocalDateTime.now());
        LearningPlan updatedPlan = learningPlanRepository.save(learningPlan);
        log.info("Learning plan updated successfully: {}", learningPlan.getId());
        return updatedPlan;
    }
    
    @Override
    public void deleteLearningPlan(Long planId) {
        log.info("Deleting learning plan with ID: {}", planId);
        learningPlanRepository.deleteById(planId);
        log.info("Learning plan deleted successfully: {}", planId);
    }
    
    @Override
    public LearningPlanItem addItemToPlan(LearningPlanItem item, Long planId) {
        log.info("Adding item to learning plan ID: {}", planId);
        LearningPlan plan = learningPlanRepository.findById(planId)
                .orElseThrow(() -> {
                    log.error("Learning plan not found with ID: {}", planId);
                    return new RuntimeException("Learning plan not found with id: " + planId);
                });
        
        item.setLearningPlan(plan);
        // Note: The @PrePersist will handle setting createdAt and updatedAt
        
        LearningPlanItem savedItem = learningPlanItemRepository.save(item);
        log.info("Item added successfully to plan with ID: {}", planId);
        return savedItem;
    }
    
    @Override
    public void removeItemFromPlan(Long itemId) {
        log.info("Removing item ID: {} from learning plan", itemId);
        learningPlanItemRepository.deleteById(itemId);
        log.info("Item removed successfully: {}", itemId);
    }
}
