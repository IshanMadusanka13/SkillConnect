package com.skillconnect.server.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "learning_update")
public class LearningUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "update_id")
    private int updateId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "category")
    private String category;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "learning_method")
    private String learningMethod;

    @Column(name = "level")
    private String level;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}