package com.skillconnect.server.service.impl;

import com.skillconnect.server.model.LearningPlan;
import com.skillconnect.server.model.LearningPlanItem;
import com.skillconnect.server.repository.LearningPlanItemRepository;
import com.skillconnect.server.repository.LearningPlanRepository;
import com.skillconnect.server.service.LearningPlanItemService;
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
public class LearningPlanItemServiceImpl implements LearningPlanItemService {

    private final LearningPlanItemRepository learningPlanItemRepository;
    private final LearningPlanRepository learningPlanRepository;
    
    @Autowired
    public LearningPlanItemServiceImpl(
            LearningPlanItemRepository learningPlanItemRepository,
            LearningPlanRepository learningPlanRepository) {
        this.learningPlanItemRepository = learningPlanItemRepository;
        this.learningPlanRepository = learningPlanRepository;
        log.info("LearningPlanItemServiceImpl initialized");
    }
    
    @Override
    public LearningPlanItem createItem(LearningPlanItem item, Long planId) {
        log.info("Creating new learning plan item for plan ID: {}", planId);
        
        LearningPlan plan = learningPlanRepository.findById(planId)
                .orElseThrow(() -> {
                    log.error("Learning plan not found with ID: {}", planId);
                    return new RuntimeException("Learning plan not found with id: " + planId);
                });
        
        item.setLearningPlan(plan);
        // The @PrePersist will handle setting createdAt and updatedAt
        
        LearningPlanItem savedItem = learningPlanItemRepository.save(item);
        log.info("Learning plan item created successfully with ID: {}", savedItem.getId());
        return savedItem;
    }
    
    @Override
    public Optional<LearningPlanItem> findById(Long itemId) {
        log.debug("Finding learning plan item by ID: {}", itemId);
        return learningPlanItemRepository.findById(itemId);
    }
    
    @Override
    public List<LearningPlanItem> findItemsByPlanId(Long planId) {
        log.debug("Finding learning plan items for plan ID: {}", planId);
        List<LearningPlanItem> items = learningPlanItemRepository.findByLearningPlanId(planId);
        log.debug("Found {} items for plan ID: {}", items.size(), planId);
        return items;
    }
    
    @Override
    public LearningPlanItem updateItem(LearningPlanItem item) {
        log.info("Updating learning plan item with ID: {}", item.getId());
        if (!learningPlanItemRepository.existsById(item.getId())) {
            log.error("Learning plan item not found with ID: {}", item.getId());
            throw new RuntimeException("Learning plan item not found with id: " + item.getId());
        }
        
        item.setUpdatedAt(LocalDateTime.now());
        LearningPlanItem updatedItem = learningPlanItemRepository.save(item);
        log.info("Learning plan item updated successfully: {}", item.getId());
        return updatedItem;
    }
    
    @Override
    public void deleteItem(Long itemId) {
        log.info("Deleting learning plan item with ID: {}", itemId);
        learningPlanItemRepository.deleteById(itemId);
        log.info("Learning plan item deleted successfully: {}", itemId);
    }
    
    @Override
    public LearningPlanItem markItemAsCompleted(Long itemId) {
        log.info("Marking learning plan item ID: {} as completed", itemId);
        
        LearningPlanItem item = learningPlanItemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.error("Learning plan item not found with ID: {}", itemId);
                    return new RuntimeException("Learning plan item not found with id: " + itemId);
                });
        
        item.setCompleted(true);
        item.setCompletedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
        
        LearningPlanItem updatedItem = learningPlanItemRepository.save(item);
        log.info("Learning plan item marked as completed: {}", itemId);
        return updatedItem;
    }
    
    @Override
    public LearningPlanItem updateItemProgress(Long itemId, int progressPercentage) {
        log.info("Updating progress for learning plan item ID: {} to {}%", itemId, progressPercentage);
        
        if (progressPercentage < 0 || progressPercentage > 100) {
            log.error("Invalid progress percentage: {}. Must be between 0 and 100", progressPercentage);
            throw new IllegalArgumentException("Progress percentage must be between 0 and 100");
        }
        
        LearningPlanItem item = learningPlanItemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.error("Learning plan item not found with ID: {}", itemId);
                    return new RuntimeException("Learning plan item not found with id: " + itemId);
                });
        
        item.setProgressPercentage(progressPercentage);
        item.setUpdatedAt(LocalDateTime.now());
        
        // If progress is 100%, mark as completed
        if (progressPercentage == 100 && !item.isCompleted()) {
            item.setCompleted(true);
            item.setCompletedAt(LocalDateTime.now());
            log.info("Learning plan item automatically marked as completed due to 100% progress: {}", itemId);
        }
        
        LearningPlanItem updatedItem = learningPlanItemRepository.save(item);
        log.info("Learning plan item progress updated successfully: {}", itemId);
        return updatedItem;
    }
    
    @Override
    public List<LearningPlanItem> findCompletedItems(Long planId) {
        log.debug("Finding completed items for plan ID: {}", planId);
        List<LearningPlanItem> completedItems = learningPlanItemRepository.findByLearningPlanIdAndCompletedTrue(planId);
        log.debug("Found {} completed items for plan ID: {}", completedItems.size(), planId);
        return completedItems;
    }
    
    @Override
    public List<LearningPlanItem> findIncompleteItems(Long planId) {
        log.debug("Finding incomplete items for plan ID: {}", planId);
        List<LearningPlanItem> incompleteItems = learningPlanItemRepository.findByLearningPlanIdAndCompletedFalse(planId);
        log.debug("Found {} incomplete items for plan ID: {}", incompleteItems.size(), planId);
        return incompleteItems;
    }
    
    @Override
    public int calculatePlanProgress(Long planId) {
        log.debug("Calculating overall progress for plan ID: {}", planId);
        
        List<LearningPlanItem> allItems = learningPlanItemRepository.findByLearningPlanId(planId);
        
        if (allItems.isEmpty()) {
            log.debug("No items found for plan ID: {}, progress is 0%", planId);
            return 0;
        }
        
        int totalItems = allItems.size();
        int totalProgress = allItems.stream()
                .mapToInt(LearningPlanItem::getProgressPercentage)
                .sum();
        
        int overallProgress = totalProgress / totalItems;
        log.debug("Overall progress for plan ID: {} is {}%", planId, overallProgress);
        return overallProgress;
    }
}
