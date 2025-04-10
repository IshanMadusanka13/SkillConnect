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
@Table(name = "Media")
public class Media {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_id")
    private int mediaId;
    
    @Column(name = "post_id", insertable = false, updatable = false)
    private int postId;
    
    @Column(name = "file_path", nullable = false)
    private String filePath;
    
    @Column(name = "media_type", nullable = false)
    private String mediaType;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
