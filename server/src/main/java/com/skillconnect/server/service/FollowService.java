package com.skillconnect.server.service;

import com.skillconnect.server.model.Follow;
import com.skillconnect.server.model.User;

import java.util.List;
import java.util.Optional;

public interface FollowService {
    
    Follow saveFollow(Follow follow);
    
    Follow followUser(Long followerId, Long followingId);
    
    void unfollowUser(Long followerId, Long followingId);
    
    Optional<Follow> findById(Long followId);
    
    List<Follow> findFollowersByUser(User user);
    
    List<Follow> findFollowingByUser(User user);
    
    long getFollowerCount(Long userId);
    
    long getFollowingCount(Long userId);
    
    boolean isFollowing(Long followerId, Long followingId);
}
