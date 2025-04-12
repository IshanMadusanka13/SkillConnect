package com.skillconnect.server.service.serviceImpl;

import com.skillconnect.server.model.UpdateTemplate;
import com.skillconnect.server.repository.UpdateTemplateRepository;
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
    
    @Autowired
    public UpdateTemplateServiceImpl(
            UpdateTemplateRepository updateTemplateRepository) {
        this.updateTemplateRepository = updateTemplateRepository;
        log.info("UpdateTemplateServiceImpl initialized");
    }
    
    @Override
    public UpdateTemplate createTemplate(UpdateTemplate template) {
        log.info("Creating new update template by creator ID: {}");
        
        UpdateTemplate savedTemplate = updateTemplateRepository.save(template);
        log.info("Update template created successfully with ID: {}", savedTemplate.getTemplateId());
        return savedTemplate;
    }
    
    @Override
    public Optional<UpdateTemplate> findById(int templateId) {
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
    public UpdateTemplate updateTemplate(UpdateTemplate template) {
        log.info("Updating update template with ID: {}", template.getTemplateId());
        if (!updateTemplateRepository.existsById(template.getTemplateId())) {
            log.error("Update template not found with ID: {}", template.getTemplateId());
            throw new RuntimeException("Update template not found with id: " + template.getTemplateId());
        }
        
        UpdateTemplate updatedTemplate = updateTemplateRepository.save(template);
        log.info("Update template updated successfully: {}", template.getTemplateId());
        return updatedTemplate;
    }
    
    @Override
    public void deleteTemplate(int templateId) {
        log.info("Deleting update template with ID: {}", templateId);
        updateTemplateRepository.deleteById(templateId);
        log.info("Update template deleted successfully: {}", templateId);
    }
}
