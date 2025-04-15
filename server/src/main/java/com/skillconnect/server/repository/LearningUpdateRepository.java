package com.skillconnect.server.repository;

import com.skillconnect.server.model.LearningUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningUpdateRepository extends JpaRepository<LearningUpdate, Integer> {
    List<LearningUpdate> findByUser_UserId(int userId);
    List<LearningUpdate> findByCategory(String category);
    List<LearningUpdate> findByType(String type);
    List<LearningUpdate> findByLevel(String level);
}
