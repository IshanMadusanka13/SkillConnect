package com.skillconnect.server.service;

import com.skillconnect.server.model.Like;
import com.skillconnect.server.model.Post;
import com.skillconnect.server.model.User;

import java.util.List;
import java.util.Optional;

public interface LikeService {
    
    Like saveLike(Like like);
    
    Like likePost(Long postId, Long userId);
    
    void unlikePost(Long postId, Long userId);
    
    Optional<Like> findById(Long likeId);
    
    List<Like> findLikesByPost(Post post);
    
    List<Like> findLikesByPostId(Long postId);
    
    List<Like> findLikesByUser(User user);
    
    long getLikeCount(Long postId);
    
    boolean hasUserLikedPost(Long userId, Long postId);
}
