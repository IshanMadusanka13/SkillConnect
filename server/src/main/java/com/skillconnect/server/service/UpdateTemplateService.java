package com.skillconnect.server.service;

import com.skillconnect.server.model.UpdateTemplate;
import com.skillconnect.server.model.User;

import java.util.List;
import java.util.Optional;

public interface UpdateTemplateService {
    
    UpdateTemplate saveUpdateTemplate(UpdateTemplate updateTemplate);
    
    Optional<UpdateTemplate> findById(Long templateId);
    
    List<UpdateTemplate> findAllUpdateTemplates();
    
    List<UpdateTemplate> findUpdateTemplatesByUser(User user);
    
    UpdateTemplate updateUpdateTemplate(UpdateTemplate updateTemplate);
    
    void deleteUpdateTemplate(Long templateId);
}
