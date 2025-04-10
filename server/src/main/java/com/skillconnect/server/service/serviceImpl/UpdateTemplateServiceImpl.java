package com.skillconnect.server.service.impl;

import com.skillconnect.server.model.UpdateTemplate;
import com.skillconnect.server.model.User;
import com.skillconnect.server.repository.UpdateTemplateRepository;
import com.skillconnect.server.repository.UserRepository;
import com.skillconnect.server.service.UpdateTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@Transactional
public class UpdateTemplateServiceImpl implements UpdateTemplateService {

    private final UpdateTemplateRepository updateTemplateRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public UpdateTemplateServiceImpl(
            UpdateTemplateRepository updateTemplateRepository,
            UserRepository userRepository) {
        this.updateTemplateRepository = updateTemplateRepository;
        this.userRepository = userRepository;
        log.info("UpdateTemplateServiceImpl initialized");
    }
    
    @Override
    public UpdateTemplate createTemplate(UpdateTemplate template, Long creatorId) {
        log.info("Creating new update template by creator ID: {}", creatorId);
        
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> {
                    log.error("Creator user not found with ID: {}", creatorId);
                    return new RuntimeException("Creator user not found with id: " + creatorId);
                });
        
        template.setCreator(creator);
        // The @PrePersist will handle setting createdAt
        
        UpdateTemplate savedTemplate = updateTemplateRepository.save(template);
        log.info("Update template created successfully with ID: {}", savedTemplate.getId());
        return savedTemplate;
    }
    
    @Override
    public Optional<UpdateTemplate> findById(Long templateId) {
        log.debug("Finding update template by ID: {}", templateId);
        return updateTemplateRepository.findById(templateId);
    }
    
    @Override
    public List<UpdateTemplate> findAllTemplates() {
        log.debug("Retrieving all update templates");
        List<UpdateTemplate> templates = updateTemplateRepository.findAll();
        log.debug("Found {} update templates", templates.size());
        return templates;
    }
    
    @Override
    public List<UpdateTemplate> findTemplatesByCreatorId(Long creatorId) {
        log.debug("Finding update templates by creator ID: {}", creatorId);
        List<UpdateTemplate> templates = updateTemplateRepository.findByCreatorId(creatorId);
        log.debug("Found {} templates by creator ID: {}", templates.size(), creatorId);
        return templates;
    }
    
    @Override
    public UpdateTemplate updateTemplate(UpdateTemplate template) {
        log.info("Updating update template with ID: {}", template.getId());
        if (!updateTemplateRepository.existsById(template.getId())) {
            log.error("Update template not found with ID: {}", template.getId());
            throw new RuntimeException("Update template not found with id: " + template.getId());
        }
        
        UpdateTemplate updatedTemplate = updateTemplateRepository.save(template);
        log.info("Update template updated successfully: {}", template.getId());
        return updatedTemplate;
    }
    
    @Override
    public void deleteTemplate(Long templateId) {
        log.info("Deleting update template with ID: {}", templateId);
        updateTemplateRepository.deleteById(templateId);
        log.info("Update template deleted successfully: {}", templateId);
    }
    
    @Override
    public List<UpdateTemplate> findPublicTemplates() {
        log.debug("Finding public update templates");
        List<UpdateTemplate> publicTemplates = updateTemplateRepository.findByIsPublicTrue();
        log.debug("Found {} public update templates", publicTemplates.size());
        return publicTemplates;
    }
    
    @Override
    public List<UpdateTemplate> findTemplatesByCategory(String category) {
        log.debug("Finding update templates by category: {}", category);
        List<UpdateTemplate> templates = updateTemplateRepository.findByCategory(category);
        log.debug("Found {} templates in category: {}", templates.size(), category);
        return templates;
    }
    
    @Override
    public UpdateTemplate setTemplatePublic(Long templateId, boolean isPublic) {
        log.info("Setting update template ID: {} public status to: {}", templateId, isPublic);
        
        UpdateTemplate template = updateTemplateRepository.findById(templateId)
                .orElseThrow(() -> {
                    log.error("Update template not found with ID: {}", templateId);
                    return new RuntimeException("Update template not found with id: " + templateId);
                });
        
        template.setPublic(isPublic);
        UpdateTemplate updatedTemplate = updateTemplateRepository.save(template);
        log.info("Update template public status updated successfully: {}", templateId);
        return updatedTemplate;
    }
    
    @Override
    public List<String> findAllCategories() {
        log.debug("Finding all distinct template categories");
        List<String> categories = updateTemplateRepository.findDistinctCategories();
        log.debug("Found {} distinct template categories", categories.size());
        return categories;
    }
    
    @Override
    public int countTemplatesByCreator(Long creatorId) {
        log.debug("Counting templates by creator ID: {}", creatorId);
        int count = updateTemplateRepository.countByCreatorId(creatorId);
        log.debug("Creator ID: {} has created {} templates", creatorId, count);
        return count;
    }
    
    @Override
    public List<UpdateTemplate> findPopularTemplates(int limit) {
        log.debug("Finding top {} popular templates", limit);
        List<UpdateTemplate> popularTemplates = updateTemplateRepository.findTopNByUsageCount(limit);
        log.debug("Found {} popular templates", popularTemplates.size());
        return popularTemplates;
    }
    
    @Override
    public UpdateTemplate incrementTemplateUsage(Long templateId) {
        log.info("Incrementing usage count for template ID: {}", templateId);
        
        UpdateTemplate template = updateTemplateRepository.findById(templateId)
                .orElseThrow(() -> {
                    log.error("Update template not found with ID: {}", templateId);
                    return new RuntimeException("Update template not found with id: " + templateId);
                });
        
        template.setUsageCount(template.getUsageCount() + 1);
        UpdateTemplate updatedTemplate = updateTemplateRepository.save(template);
        log.info("Template usage count incremented for ID: {}", templateId);
        return updatedTemplate;
    }
}
