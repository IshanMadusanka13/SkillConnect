package com.skillconnect.server.service;

import com.skillconnect.server.model.UpdateTemplate;

import java.util.List;
import java.util.Optional;

public interface UpdateTemplateService {

    UpdateTemplate createTemplate(UpdateTemplate template);

    List<UpdateTemplate> findAllTemplates();
    
    Optional<UpdateTemplate> findById(int templateId);

    UpdateTemplate updateTemplate(UpdateTemplate template);

    void deleteTemplate(int templateId);
}
