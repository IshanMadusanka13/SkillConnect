package com.skillconnect.server.controller;

import com.skillconnect.server.model.LearningPlan;
import com.skillconnect.server.model.LearningPlanItem;
import com.skillconnect.server.service.LearningPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learning-plans")
public class LearningPlanController {

    private final LearningPlanService learningPlanService;

    @Autowired
    public LearningPlanController(LearningPlanService learningPlanService) {
        this.learningPlanService = learningPlanService;
    }

    @PostMapping
    public ResponseEntity<LearningPlan> createLearningPlan(@RequestBody LearningPlan learningPlan) {
        LearningPlan created = learningPlanService.createLearningPlan(learningPlan);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LearningPlan> getLearningPlanById(@PathVariable int id) {
        return learningPlanService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LearningPlan>> getLearningPlansByUserId(@PathVariable int userId) {
        return ResponseEntity.ok(learningPlanService.findLearningPlansByUserId(userId));
    }

    @PutMapping
    public ResponseEntity<LearningPlan> updateLearningPlan(@RequestBody LearningPlan learningPlan) {
        return ResponseEntity.ok(learningPlanService.updateLearningPlan(learningPlan));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLearningPlan(@PathVariable int id) {
        learningPlanService.deleteLearningPlan(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{planId}/items")
    public ResponseEntity<LearningPlanItem> addItemToPlan(
            @RequestBody LearningPlanItem item,
            @PathVariable int planId) {
        LearningPlanItem addedItem = learningPlanService.addItemToPlan(item, planId);
        return ResponseEntity.ok(addedItem);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItemFromPlan(@PathVariable int itemId) {
        learningPlanService.removeItemFromPlan(itemId);
        return ResponseEntity.noContent().build();
    }
}
