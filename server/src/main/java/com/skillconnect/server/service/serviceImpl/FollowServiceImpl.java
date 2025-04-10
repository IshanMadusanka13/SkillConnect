package com.skillconnect.server.service.impl;

import com.skillconnect.server.model.Follow;
import com.skillconnect.server.model.User;
import com.skillconnect.server.repository.FollowRepository;
import com.skillconnect.server.repository.UserRepository;
import com.skillconnect.server.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@Transactional
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public FollowServiceImpl(
            FollowRepository followRepository,
            UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        log.info("FollowServiceImpl initialized");
    }
    
    @Override
    public Follow followUser(Long followerId, Long followedId) {
        log.info("Creating follow relationship: follower ID {} following user ID {}", followerId, followedId);
        
        // Check if users exist
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> {
                    log.error("Follower user not found with ID: {}", followerId);
                    return new RuntimeException("Follower user not found with id: " + followerId);
                });
        
        User followed = userRepository.findById(followedId)
                .orElseThrow(() -> {
                    log.error("Followed user not found with ID: {}", followedId);
                    return new RuntimeException("Followed user not found with id: " + followedId);
                });
        
        // Check if already following
        if (isFollowing(followerId, followedId)) {
            log.warn("User ID {} is already following user ID {}", followerId, followedId);
            throw new RuntimeException("Already following this user");
        }
        
        // Create new follow relationship
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowed(followed);
        // The @PrePersist will handle setting createdAt
        
        Follow savedFollow = followRepository.save(follow);
        log.info("Follow relationship created successfully with ID: {}", savedFollow.getId());
        return savedFollow;
    }
    
    @Override
    public void unfollowUser(Long followerId, Long followedId) {
        log.info("Removing follow relationship: follower ID {} unfollowing user ID {}", followerId, followedId);
        
        Follow follow = followRepository.findByFollowerIdAndFollowedId(followerId, followedId)
                .orElseThrow(() -> {
                    log.error("Follow relationship not found between follower ID {} and followed ID {}", 
                            followerId, followedId);
                    return new RuntimeException("Follow relationship not found");
                });
        
        followRepository.delete(follow);
        log.info("Follow relationship removed successfully");
    }
    
    @Override
    public boolean isFollowing(Long followerId, Long followedId) {
        log.debug("Checking if user ID {} is following user ID {}", followerId, followedId);
        boolean following = followRepository.existsByFollowerIdAndFollowedId(followerId, followedId);
        log.debug("User ID {} is following user ID {}: {}", followerId, followedId, following);
        return following;
    }
    
    @Override
    public List<Follow> getFollowerRelationships(Long userId) {
        log.debug("Getting follower relationships for user ID: {}", userId);
        List<Follow> followers = followRepository.findByFollowedId(userId);
        log.debug("Found {} follower relationships for user ID: {}", followers.size(), userId);
        return followers;
    }
    
    @Override
    public List<Follow> getFollowingRelationships(Long userId) {
        log.debug("Getting following relationships for user ID: {}", userId);
        List<Follow> following = followRepository.findByFollowerId(userId);
        log.debug("Found {} following relationships for user ID: {}", following.size(), userId);
        return following;
    }
    
    @Override
    public int getFollowerCount(Long userId) {
        log.debug("Getting follower count for user ID: {}", userId);
        int count = followRepository.countByFollowedId(userId);
        log.debug("User ID {} has {} followers", userId, count);
        return count;
    }
    
    @Override
    public int getFollowingCount(Long userId) {
        log.debug("Getting following count for user ID: {}", userId);
        int count = followRepository.countByFollowerId(userId);
        log.debug("User ID {} is following {} users", userId, count);
        return count;
    }
    
    @Override
    public List<Follow> findMutualFollows(Long userId1, Long userId2) {
        log.debug("Finding mutual follows between user ID {} and user ID {}", userId1, userId2);
        List<Follow> mutualFollows = followRepository.findMutualFollows(userId1, userId2);
        log.debug("Found {} mutual follows", mutualFollows.size());
        return mutualFollows;
    }
    
    @Override
    public Optional<Follow> findById(Long followId) {
        log.debug("Finding follow relationship by ID: {}", followId);
        return followRepository.findById(followId);
    }
}
