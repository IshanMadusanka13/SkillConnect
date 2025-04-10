package com.skillconnect.server.service.impl;

import com.skillconnect.server.model.Like;
import com.skillconnect.server.model.Post;
import com.skillconnect.server.model.Comment;
import com.skillconnect.server.model.User;
import com.skillconnect.server.repository.LikeRepository;
import com.skillconnect.server.repository.PostRepository;
import com.skillconnect.server.repository.CommentRepository;
import com.skillconnect.server.repository.UserRepository;
import com.skillconnect.server.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@Transactional
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public LikeServiceImpl(
            LikeRepository likeRepository,
            PostRepository postRepository,
            CommentRepository commentRepository,
            UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        log.info("LikeServiceImpl initialized");
    }
    
    @Override
    public Like likePost(Long userId, Long postId) {
        log.info("Creating like for post ID: {} by user ID: {}", postId, userId);
        
        // Check if already liked
        if (hasUserLikedPost(userId, postId)) {
            log.warn("User ID: {} has already liked post ID: {}", userId, postId);
            throw new RuntimeException("User has already liked this post");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new RuntimeException("User not found with id: " + userId);
                });
        
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> {
                    log.error("Post not found with ID: {}", postId);
                    return new RuntimeException("Post not found with id: " + postId);
                });
        
        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        like.setCreatedAt(LocalDateTime.now());
        
        Like savedLike = likeRepository.save(like);
        log.info("Like created successfully with ID: {}", savedLike.getId());
        return savedLike;
    }
    
    @Override
    public Like likeComment(Long userId, Long commentId) {
        log.info("Creating like for comment ID: {} by user ID: {}", commentId, userId);
        
        // Check if already liked
        if (hasUserLikedComment(userId, commentId)) {
            log.warn("User ID: {} has already liked comment ID: {}", userId, commentId);
            throw new RuntimeException("User has already liked this comment");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new RuntimeException("User not found with id: " + userId);
                });
        
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    log.error("Comment not found with ID: {}", commentId);
                    return new RuntimeException("Comment not found with id: " + commentId);
                });
        
        Like like = new Like();
        like.setUser(user);
        like.setComment(comment);
        like.setCreatedAt(LocalDateTime.now());
        
        Like savedLike = likeRepository.save(like);
        log.info("Like created successfully with ID: {}", savedLike.getId());
        return savedLike;
    }
    
    @Override
    public void unlikePost(Long userId, Long postId) {
        log.info("Removing like for post ID: {} by user ID: {}", postId, userId);
        
        Like like = likeRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> {
                    log.error("Like not found for post ID: {} by user ID: {}", postId, userId);
                    return new RuntimeException("Like not found");
                });
        
        likeRepository.delete(like);
        log.info("Like removed successfully");
    }
    
    @Override
    public void unlikeComment(Long userId, Long commentId) {
        log.info("Removing like for comment ID: {} by user ID: {}", commentId, userId);
        
        Like like = likeRepository.findByUserIdAndCommentId(userId, commentId)
                .orElseThrow(() -> {
                    log.error("Like not found for comment ID: {} by user ID: {}", commentId, userId);
                    return new RuntimeException("Like not found");
                });
        
        likeRepository.delete(like);
        log.info("Like removed successfully");
    }
    
    @Override
    public boolean hasUserLikedPost(Long userId, Long postId) {
        log.debug("Checking if user ID: {} has liked post ID: {}", userId, postId);
        boolean hasLiked = likeRepository.existsByUserIdAndPostId(userId, postId);
        log.debug("User ID: {} has liked post ID: {}: {}", userId, postId, hasLiked);
        return hasLiked;
    }
    
    @Override
    public boolean hasUserLikedComment(Long userId, Long commentId) {
        log.debug("Checking if user ID: {} has liked comment ID: {}", userId, commentId);
        boolean hasLiked = likeRepository.existsByUserIdAndCommentId(userId, commentId);
        log.debug("User ID: {} has liked comment ID: {}: {}", userId, commentId, hasLiked);
        return hasLiked;
    }
    
    @Override
    public int countPostLikes(Long postId) {
        log.debug("Counting likes for post ID: {}", postId);
        int count = likeRepository.countByPostId(postId);
        log.debug("Post ID: {} has {} likes", postId, count);
        return count;
    }
    
    @Override
    public int countCommentLikes(Long commentId) {
        log.debug("Counting likes for comment ID: {}", commentId);
        int count = likeRepository.countByCommentId(commentId);
        log.debug("Comment ID: {} has {} likes", commentId, count);
        return count;
    }
    
    @Override
    public List<Like> findLikesByPostId(Long postId) {
        log.debug("Finding likes for post ID: {}", postId);
        List<Like> likes = likeRepository.findByPostId(postId);
        log.debug("Found {} likes for post ID: {}", likes.size(), postId);
        return likes;
    }
    
    @Override
    public List<Like> findLikesByCommentId(Long commentId) {
        log.debug("Finding likes for comment ID: {}", commentId);
        List<Like> likes = likeRepository.findByCommentId(commentId);
        log.debug("Found {} likes for comment ID: {}", likes.size(), commentId);
        return likes;
    }
    
    @Override
    public List<Like> findLikesByUserId(Long userId) {
        log.debug("Finding likes by user ID: {}", userId);
        List<Like> likes = likeRepository.findByUserId(userId);
        log.debug("Found {} likes by user ID: {}", likes.size(), userId);
        return likes;
    }
    
    @Override
    public Optional<Like> findById(Long likeId) {
        log.debug("Finding like by ID: {}", likeId);
        return likeRepository.findById(likeId);
    }
}
