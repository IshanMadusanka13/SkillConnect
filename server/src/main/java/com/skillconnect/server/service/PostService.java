package com.skillconnect.server.service;

import com.skillconnect.server.model.Post;
import com.skillconnect.server.model.User;

import java.util.List;
import java.util.Optional;

public interface PostService {
    
    Post savePost(Post post);
    
    Optional<Post> findById(Long postId);
    
    List<Post> findAllPosts();
    
    List<Post> findPostsByUser(User user);
    
    List<Post> findPostsByUserId(Long userId);
    
    Post updatePost(Post post);
    
    void deletePost(Long postId);
    
    List<Post> getTimelinePosts(Long userId);
    
    long getPostCount(Long userId);
    
    boolean isPostOwner(Long postId, Long userId);
}
