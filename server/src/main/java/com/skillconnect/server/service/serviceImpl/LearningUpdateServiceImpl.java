package com.skillconnect.server.service.impl;

import com.skillconnect.server.model.LearningPlan;
import com.skillconnect.server.model.LearningUpdate;
import com.skillconnect.server.model.UpdateTemplate;
import com.skillconnect.server.model.User;
import com.skillconnect.server.repository.LearningPlanRepository;
import com.skillconnect.server.repository.LearningUpdateRepository;
import com.skillconnect.server.repository.UpdateTemplateRepository;
import com.skillconnect.server.repository.UserRepository;
import com.skillconnect.server.service.LearningUpdateService;
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
public class LearningUpdateServiceImpl implements LearningUpdateService {

    private final LearningUpdateRepository learningUpdateRepository;
    private final LearningPlanRepository learningPlanRepository;
    private final UserRepository userRepository;
    private final UpdateTemplateRepository updateTemplateRepository;
    
    @Autowired
    public LearningUpdateServiceImpl(
            LearningUpdateRepository learningUpdateRepository,
            LearningPlanRepository learningPlanRepository,
            UserRepository userRepository,
            UpdateTemplateRepository updateTemplateRepository) {
        this.learningUpdateRepository = learningUpdateRepository;
        this.learningPlanRepository = learningPlanRepository;
        this.userRepository = userRepository;
        this.updateTemplateRepository = updateTemplateRepository;
        log.info("LearningUpdateServiceImpl initialized");
    }
    
    @Override
    public LearningUpdate createUpdate(LearningUpdate update, Long userId, Long planId) {
        log.info("Creating new learning update for user ID: {} and plan ID: {}", userId, planId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new RuntimeException("User not found with id: " + userId);
                });
        
        LearningPlan plan = learningPlanRepository.findById(planId)
                .orElseThrow(() -> {
                    log.error("Learning plan not found with ID: {}", planId);
                    return new RuntimeException("Learning plan not found with id: " + planId);
                });
        
        // Verify the plan belongs to the user
        if (!plan.getUser().getId().equals(userId)) {
            log.error("Learning plan ID: {} does not belong to user ID: {}", planId, userId);
            throw new RuntimeException("Learning plan does not belong to this user");
        }
        
        update.setUser(user);
        update.setLearningPlan(plan);
        // The @PrePersist will handle setting createdAt and updatedAt
        
        LearningUpdate savedUpdate = learningUpdateRepository.save(update);
        log.info("Learning update created successfully with ID: {}", savedUpdate.getId());
        return savedUpdate;
    }
    
    @Override
    public LearningUpdate createUpdateFromTemplate(Long templateId, Long userId, Long planId, String content) {
        log.info("Creating learning update from template ID: {} for user ID: {} and plan ID: {}", 
                templateId, userId, planId);
        
        UpdateTemplate template = updateTemplateRepository.findById(templateId)
                .orElseThrow(() -> {
                    log.error("Update template not found with ID: {}", templateId);
                    return new RuntimeException("Update template not found with id: " + templateId);
                });
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new RuntimeException("User not found with id: " + userId);
                });
        
        LearningPlan plan = learningPlanRepository.findById(planId)
                .orElseThrow(() -> {
                    log.error("Learning plan not found with ID: {}", planId);
                    return new RuntimeException("Learning plan not found with id: " + planId);
                });
        
        // Verify the plan belongs to the user
        if (!plan.getUser().getId().equals(userId)) {
            log.error("Learning plan ID: {} does not belong to user ID: {}", planId, userId);
            throw new RuntimeException("Learning plan does not belong to this user");
        }
        
        LearningUpdate update = new LearningUpdate();
        update.setTitle(template.getTitle());
        update.setContent(content);
        update.setUser(user);
        update.setLearningPlan(plan);
        update.setTemplate(template);
        
        LearningUpdate savedUpdate = learningUpdateRepository.save(update);
        log.info("Learning update created from template successfully with ID: {}", savedUpdate.getId());
        return savedUpdate;
    }
    
    @Override
    public Optional<LearningUpdate> findById(Long updateId) {
        log.debug("Finding learning update by ID: {}", updateId);
        return learningUpdateRepository.findById(updateId);
    }
    
    @Override
    public List<LearningUpdate> findUpdatesByUserId(Long userId) {
        log.debug("Finding learning updates for user ID: {}", userId);
        List<LearningUpdate> updates = learningUpdateRepository.findByUserId(userId);
        log.debug("Found {} updates for user ID: {}", updates.size(), userId);
        return updates;
    }
    
    @Override
    public List<LearningUpdate> findUpdatesByPlanId(Long planId) {
        log.debug("Finding learning updates for plan ID: {}", planId);
        List<LearningUpdate> updates = learningUpdateRepository.findByLearningPlanId(planId);
        log.debug("Found {} updates for plan ID: {}", updates.size(), planId);
        return updates;
    }
    
    @Override
    public LearningUpdate updateLearningUpdate(LearningUpdate update) {
        log.info("Updating learning update with ID: {}", update.getId());
        if (!learningUpdateRepository.existsById(update.getId())) {
            log.error("Learning update not found with ID: {}", update.getId());
            throw new RuntimeException("Learning update not found with id: " + update.getId());
        }
        
        update.setUpdatedAt(LocalDateTime.now());
        LearningUpdate updatedUpdate = learningUpdateRepository.save(update);
        log.info("Learning update updated successfully: {}", update.getId());
        return updatedUpdate;
    }
    
    @Override
    public void deleteUpdate(Long updateId) {
        log.info("Deleting learning update with ID: {}", updateId);
        learningUpdateRepository.deleteById(updateId);
        log.info("Learning update deleted successfully: {}", updateId);
    }
    
    @Override
    public List<LearningUpdate> findRecentUpdates(Long userId, int limit) {
        log.debug("Finding recent {} learning updates for user ID: {}", limit, userId);
        List<LearningUpdate> recentUpdates = learningUpdateRepository.findByUserIdOrderByCreatedAtDesc(userId, limit);
        log.debug("Found {} recent updates for user ID: {}", recentUpdates.size(), userId);
        return recentUpdates;
    }
    
    @Override
    public List<LearningUpdate> findUpdatesByTemplateId(Long templateId) {
        log.debug("Finding learning updates for template ID: {}", templateId);
        List<LearningUpdate> updates = learningUpdateRepository.findByTemplateId(templateId);
        log.debug("Found {} updates for template ID: {}", updates.size(), templateId);
        return updates;
    }
    
    @Override
    public int countUpdatesByPlanId(Long planId) {
        log.debug("Counting learning updates for plan ID: {}", planId);
        int count = learningUpdateRepository.countByLearningPlanId(planId);
        log.debug("Found {} updates for plan ID: {}", count, planId);
        return count;
    }
    
    @Override
    public List<LearningUpdate> findAllUpdates() {
        log.debug("Retrieving all learning updates");
        List<LearningUpdate> updates = learningUpdateRepository.findAll();
        log.debug("Found {} learning updates", updates.size());
        return updates;
    }
}
