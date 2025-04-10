package com.skillconnect.server.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LearningPlanItems")
public class LearningPlanItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private int itemId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private LearningPlan learningPlan;
    
    @Column(name = "title", nullable = false, length = 100)
    private String title;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @ElementCollection
    @CollectionTable(
        name = "learning_plan_item_resources",
        joinColumns = @JoinColumn(name = "item_id")
    )
    @Column(name = "resource")
    private List<String> resources = new ArrayList<>();
    
    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;
    
    @Column(name = "status", nullable = false, length = 20)
    private String status = "pending";
    
    @Column(name = "completion_date")
    private LocalDate completionDate;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
}
