package com.skillconnect.server.service.serviceImpl;

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
    public LearningUpdate createUpdate(LearningUpdate update, int planId) {
        log.info("Creating new learning update for user ID: {} and plan ID: {}", update.getUser().getUserId(), planId);
        
        User user = userRepository.findById(update.getUser().getUserId())
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", update.getUser().getUserId());
                    return new RuntimeException("User not found with id: " + update.getUser().getUserId());
                });
        
        LearningPlan plan = learningPlanRepository.findById(planId)
                .orElseThrow(() -> {
                    log.error("Learning plan not found with ID: {}", planId);
                    return new RuntimeException("Learning plan not found with id: " + planId);
                });
        
        // Verify the plan beints to the user
        if (plan.getUser().getUserId() != update.getUser().getUserId()) {
            log.error("Learning plan ID: {} does not beint to user ID: {}", planId, update.getUser().getUserId());
            throw new RuntimeException("Learning plan does not beint to this user");
        }
        
        update.setUser(user);
        // The @PrePersist will handle setting createdAt and updatedAt
        
        LearningUpdate savedUpdate = learningUpdateRepository.save(update);
        log.info("Learning update created successfully with ID: {}", savedUpdate.getUpdateId());
        return savedUpdate;
    }
    
    @Override
    public LearningUpdate createUpdateFromTemplate(UpdateTemplate template, LearningPlan plan) {
        log.info("Creating learning update from template ID: {} for user ID: {} and plan ID: {}", 
                template.getTemplateId(), plan.getUser().getUserId(), plan.getPlanId());
        
        UpdateTemplate templateExist = updateTemplateRepository.findById(template.getTemplateId())
                .orElseThrow(() -> {
                    log.error("Update template not found with ID: {}", template.getTemplateId());
                    return new RuntimeException("Update template not found with id: " + template.getTemplateId());
                });
        
        User user = userRepository.findById(plan.getUser().getUserId())
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", plan.getUser().getUserId());
                    return new RuntimeException("User not found with id: " + plan.getUser().getUserId());
                });
        
        LearningPlan planExist = learningPlanRepository.findById(plan.getUser().getUserId())
                .orElseThrow(() -> {
                    log.error("Learning plan not found with ID: {}", plan.getPlanId());
                    return new RuntimeException("Learning plan not found with id: " + plan.getPlanId());
                });
        
        // Verify the plan beints to the user
        if (planExist.getUser().getUserId() != plan.getUser().getUserId()) {
            log.error("Learning plan ID: {} does not beint to user ID: {}", plan.getPlanId(), plan.getUser().getUserId());
            throw new RuntimeException("Learning plan does not beint to this user");
        }
        
        LearningUpdate update = new LearningUpdate();
        update.setTitle(templateExist.getName());
        update.setDescription(templateExist.getDescription());
        update.setUser(user);
        
        LearningUpdate savedUpdate = learningUpdateRepository.save(update);
        log.info("Learning update created from template successfully with ID: {}", savedUpdate.getUpdateId());
        return savedUpdate;
    }
    
    @Override
    public Optional<LearningUpdate> findById(int updateId) {
        log.debug("Finding learning update by ID: {}", updateId);
        return learningUpdateRepository.findById(updateId);
    }
    
    @Override
    public List<LearningUpdate> findUpdatesByUserId(int userId) {
        log.debug("Finding learning updates for user ID: {}", userId);
        List<LearningUpdate> updates = learningUpdateRepository.findByUser_UserId(userId);
        log.debug("Found {} updates for user ID: {}", updates.size(), userId);
        return updates;
    }
    
    @Override
    public LearningUpdate updateLearningUpdate(LearningUpdate update) {
        log.info("Updating learning update with ID: {}", update.getUpdateId());
        if (!learningUpdateRepository.existsById(update.getUpdateId())) {
            log.error("Learning update not found with ID: {}", update.getUpdateId());
            throw new RuntimeException("Learning update not found with id: " + update.getUpdateId());
        }
        
        update.setUpdatedAt(LocalDateTime.now());
        LearningUpdate updatedUpdate = learningUpdateRepository.save(update);
        log.info("Learning update updated successfully: {}", update.getUpdateId());
        return updatedUpdate;
    }
    
    @Override
    public void deleteUpdate(int updateId) {
        log.info("Deleting learning update with ID: {}", updateId);
        learningUpdateRepository.deleteById(updateId);
        log.info("Learning update deleted successfully: {}", updateId);
    }

}
