package com.skillconnect.server.service.serviceImpl;

import com.skillconnect.server.model.Follow;
import com.skillconnect.server.model.User;
import com.skillconnect.server.repository.FollowRepository;
import com.skillconnect.server.repository.UserRepository;
import com.skillconnect.server.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.log4j.Log4j2;

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
    public Follow followUser(Follow follow) {
        log.info("Creating follow relationship: follower ID {} following user ID {}", follow.getFollower().getUserId(), follow.getFollowing().getUserId());
        
        // Check if users exist
        User follower = userRepository.findById(follow.getFollower().getUserId())
                .orElseThrow(() -> {
                    log.error("Follower user not found with ID: {}", follow.getFollower().getUserId());
                    return new RuntimeException("Follower user not found with id: " + follow.getFollower().getUserId());
                });
        
        User followed = userRepository.findById(follow.getFollowing().getUserId())
                .orElseThrow(() -> {
                    log.error("Followed user not found with ID: {}", follow.getFollowing().getUserId());
                    return new RuntimeException("Followed user not found with id: " + follow.getFollowing().getUserId());
                });
        
        // Check if already following
        if (isFollowing(follow.getFollower().getUserId(), follow.getFollowing().getUserId())) {
            log.warn("User ID {} is already following user ID {}", follow.getFollower().getUserId(), follow.getFollowing().getUserId());
            throw new RuntimeException("Already following this user");
        }
        
        // Create new follow relationship
        Follow newFollow = new Follow();
        newFollow.setFollower(follower);
        newFollow.setFollowing(followed);
        // The @PrePersist will handle setting createdAt
        
        Follow savedFollow = followRepository.save(newFollow);
        log.info("Follow relationship created successfully with ID: {}", savedFollow.getFollowId());
        return savedFollow;
    }
    
    @Override
    public void unfollowUser(Follow follow) {
        log.info("Removing follow relationship: follower ID {} unfollowing user ID {}", follow.getFollower().getUserId(), follow.getFollowing().getUserId());
        
        Follow followExist = followRepository.findByFollower_UserIdAndFollowing_UserId(follow.getFollower().getUserId(), follow.getFollowing().getUserId())
                .orElseThrow(() -> {
                    log.error("Follow relationship not found between follower ID {} and followed ID {}", 
                    follow.getFollower().getUserId(), follow.getFollowing().getUserId());
                    return new RuntimeException("Follow relationship not found");
                });
        
        followRepository.delete(followExist);
        log.info("Follow relationship removed successfully");
    }
    
    @Override
    public boolean isFollowing(int followerId, int followedId) {
        log.debug("Checking if user ID {} is following user ID {}", followerId, followedId);
        Optional<Follow> follow = followRepository.findByFollower_UserIdAndFollowing_UserId(followerId, followedId);
        boolean following = follow.isPresent();
        log.debug("User ID {} is following user ID {}: {}", followerId, followedId, following);
        return following;
    }
    
    @Override
    public int getFollowerCount(int userId) {
        log.debug("Getting follower count for user ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        int count = followRepository.countByFollowing(user);
        log.debug("User ID {} has {} followers", userId, count);
        return count;
    }
    
    @Override
    public int getFollowingCount(int userId) {
        log.debug("Getting following count for user ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        int count = followRepository.countByFollower(user);
        log.debug("User ID {} is following {} users", userId, count);
        return count;
    }
    
}
