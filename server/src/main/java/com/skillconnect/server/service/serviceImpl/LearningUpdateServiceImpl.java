package com.skillconnect.server.service.serviceImpl;

import com.skillconnect.server.model.LearningUpdate;
import com.skillconnect.server.repository.LearningUpdateRepository;
import com.skillconnect.server.service.LearningUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class LearningUpdateServiceImpl implements LearningUpdateService {

    private final LearningUpdateRepository learningUpdateRepository;

    @Override
    public LearningUpdate saveLearningUpdate(LearningUpdate learningUpdate) {
        log.info("Saving new learning update for user ID: {}", learningUpdate.getUser().getUserId());

        // Set creation time if not already set
        if (learningUpdate.getCreatedAt() == null) {
            learningUpdate.setCreatedAt(LocalDateTime.now());
        }

        LearningUpdate savedUpdate = learningUpdateRepository.save(learningUpdate);
        log.info("Learning update saved successfully with ID: {}", savedUpdate.getUpdateId());
        return savedUpdate;
    }

    @Override
    public Optional<LearningUpdate> findById(int updateId) {
        log.debug("Finding learning update by ID: {}", updateId);
        return learningUpdateRepository.findById(updateId);
    }

    @Override
    public List<LearningUpdate> findAllLearningUpdates() {
        log.debug("Retrieving all learning updates");
        List<LearningUpdate> updates = learningUpdateRepository.findAll();
        log.debug("Found {} learning updates", updates.size());
        return updates;
    }

    @Override
    public List<LearningUpdate> findByUserId(int userId) {
        log.debug("Finding learning updates for user ID: {}", userId);
        List<LearningUpdate> updates = learningUpdateRepository.findByUser_UserId(userId);
        log.debug("Found {} learning updates for user ID: {}", updates.size(), userId);
        return updates;
    }

    @Override
    public LearningUpdate updateLearningUpdate(LearningUpdate learningUpdate) {
        log.info("Updating learning update with ID: {}", learningUpdate.getUpdateId());
        if (!learningUpdateRepository.existsById(learningUpdate.getUpdateId())) {
            log.error("Learning update not found with ID: {}", learningUpdate.getUpdateId());
            throw new RuntimeException("Learning update not found with id: " + learningUpdate.getUpdateId());
        }

        LearningUpdate updatedLearningUpdate = learningUpdateRepository.save(learningUpdate);
        log.info("Learning update updated successfully: {}", learningUpdate.getUpdateId());
        return updatedLearningUpdate;
    }

    @Override
    public void deleteLearningUpdate(int updateId) {
        log.info("Deleting learning update with ID: {}", updateId);
        learningUpdateRepository.deleteById(updateId);
        log.info("Learning update deleted successfully: {}", updateId);
    }

    @Override
    public List<LearningUpdate> findByCategory(String category) {
        log.debug("Finding learning updates by category: {}", category);
        List<LearningUpdate> updates = learningUpdateRepository.findByCategory(category);
        log.debug("Found {} learning updates for category: {}", updates.size(), category);
        return updates;
    }

    @Override
    public List<LearningUpdate> findByType(String type) {
        log.debug("Finding learning updates by type: {}", type);
        List<LearningUpdate> updates = learningUpdateRepository.findByType(type);
        log.debug("Found {} learning updates for type: {}", updates.size(), type);
        return updates;
    }

    @Override
    public List<LearningUpdate> findByLevel(String level) {
        log.debug("Finding learning updates by level: {}", level);
        List<LearningUpdate> updates = learningUpdateRepository.findByLevel(level);
        log.debug("Found {} learning updates for level: {}", updates.size(), level);
        return updates;
    }
}
