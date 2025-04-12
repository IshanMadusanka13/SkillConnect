package com.skillconnect.server.controller;

import com.skillconnect.server.model.UpdateTemplate;
import com.skillconnect.server.service.UpdateTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/update-templates")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UpdateTemplateController {

    private final UpdateTemplateService updateTemplateService;

    @PostMapping
    public ResponseEntity<UpdateTemplate> createTemplate(@RequestBody UpdateTemplate template) {
        UpdateTemplate createdTemplate = updateTemplateService.createTemplate(template);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTemplate);
    }

    @GetMapping
    public ResponseEntity<List<UpdateTemplate>> getAllTemplates() {
        List<UpdateTemplate> templates = updateTemplateService.findAllTemplates();
        return ResponseEntity.ok(templates);
    }

    @GetMapping("/{templateId}")
    public ResponseEntity<UpdateTemplate> getTemplateById(@PathVariable int templateId) {
        return updateTemplateService.findById(templateId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{templateId}")
    public ResponseEntity<UpdateTemplate> updateTemplate(@PathVariable int templateId, @RequestBody UpdateTemplate template) {
        template.setTemplateId(templateId);  // Ensure we update the correct template
        UpdateTemplate updatedTemplate = updateTemplateService.updateTemplate(template);
        return ResponseEntity.ok(updatedTemplate);
    }

    @DeleteMapping("/{templateId}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable int templateId) {
        updateTemplateService.deleteTemplate(templateId);
        return ResponseEntity.noContent().build();
    }
}

