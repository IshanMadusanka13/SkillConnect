package com.skillconnect.server.controller;

import com.skillconnect.server.model.LearningPlan;
import com.skillconnect.server.model.LearningUpdate;
import com.skillconnect.server.model.UpdateTemplate;
import com.skillconnect.server.service.LearningPlanService;
import com.skillconnect.server.service.LearningUpdateService;
import com.skillconnect.server.service.UpdateTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learning-updates")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LearningUpdateController {

    private final LearningUpdateService learningUpdateService;
    private final UpdateTemplateService updateTemplateService;
    private final LearningPlanService learningPlanService;

    @PostMapping("/from-template")
    public ResponseEntity<LearningUpdate> createFromTemplate(
            @RequestParam int templateId,
            @RequestParam int planId) {

        // Assume service can fetch the actual objects
        UpdateTemplate template = updateTemplateService.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        LearningPlan plan = learningPlanService.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        LearningUpdate update = learningUpdateService.createUpdateFromTemplate(template, plan);
        return ResponseEntity.ok(update);
    }


    @PostMapping("/plan/{planId}")
    public ResponseEntity<LearningUpdate> createUpdate(
            @RequestBody LearningUpdate update,
            @PathVariable int planId) {
        LearningUpdate created = learningUpdateService.createUpdate(update, planId);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LearningUpdate> getById(@PathVariable int id) {
        return learningUpdateService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LearningUpdate>> getUpdatesByUserId(@PathVariable int userId) {
        return ResponseEntity.ok(learningUpdateService.findUpdatesByUserId(userId));
    }

    @PutMapping
    public ResponseEntity<LearningUpdate> updateLearningUpdate(@RequestBody LearningUpdate update) {
        return ResponseEntity.ok(learningUpdateService.updateLearningUpdate(update));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUpdate(@PathVariable int id) {
        learningUpdateService.deleteUpdate(id);
        return ResponseEntity.noContent().build();
    }
}

