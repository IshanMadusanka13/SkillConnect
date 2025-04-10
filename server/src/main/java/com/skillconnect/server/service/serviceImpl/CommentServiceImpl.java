package com.skillconnect.server.service.impl;

import com.skillconnect.server.model.Comment;
import com.skillconnect.server.model.Post;
import com.skillconnect.server.model.User;
import com.skillconnect.server.repository.CommentRepository;
import com.skillconnect.server.repository.PostRepository;
import com.skillconnect.server.repository.UserRepository;
import com.skillconnect.server.service.CommentService;
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
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public CommentServiceImpl(
            CommentRepository commentRepository,
            PostRepository postRepository,
            UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        log.info("CommentServiceImpl initialized");
    }
    
    @Override
    public Comment createComment(Comment comment, Long postId, Long userId) {
        log.info("Creating new comment for post ID: {} by user ID: {}", postId, userId);
        
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> {
                    log.error("Post not found with ID: {}", postId);
                    return new RuntimeException("Post not found with id: " + postId);
                });
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new RuntimeException("User not found with id: " + userId);
                });
        
        comment.setPost(post);
        comment.setUser(user);
        // Note: The @PrePersist will handle setting createdAt and updatedAt
        
        Comment savedComment = commentRepository.save(comment);
        log.info("Comment created successfully with ID: {}", savedComment.getId());
        return savedComment;
    }
    
    @Override
    public Optional<Comment> findById(Long commentId) {
        log.debug("Finding comment by ID: {}", commentId);
        return commentRepository.findById(commentId);
    }
    
    @Override
    public List<Comment> findCommentsByPostId(Long postId) {
        log.debug("Finding comments for post ID: {}", postId);
        List<Comment> comments = commentRepository.findByPostId(postId);
        log.debug("Found {} comments for post ID: {}", comments.size(), postId);
        return comments;
    }
    
    @Override
    public List<Comment> findCommentsByUserId(Long userId) {
        log.debug("Finding comments by user ID: {}", userId);
        List<Comment> comments = commentRepository.findByUserId(userId);
        log.debug("Found {} comments by user ID: {}", comments.size(), userId);
        return comments;
    }
    
    @Override
    public Comment updateComment(Comment comment) {
        log.info("Updating comment with ID: {}", comment.getId());
        if (!commentRepository.existsById(comment.getId())) {
            log.error("Comment not found with ID: {}", comment.getId());
            throw new RuntimeException("Comment not found with id: " + comment.getId());
        }
        
        comment.setUpdatedAt(LocalDateTime.now());
        Comment updatedComment = commentRepository.save(comment);
        log.info("Comment updated successfully: {}", comment.getId());
        return updatedComment;
    }
    
    @Override
    public void deleteComment(Long commentId) {
        log.info("Deleting comment with ID: {}", commentId);
        commentRepository.deleteById(commentId);
        log.info("Comment deleted successfully: {}", commentId);
    }
    
    @Override
    public List<Comment> findAllComments() {
        log.debug("Retrieving all comments");
        List<Comment> comments = commentRepository.findAll();
        log.debug("Found {} comments", comments.size());
        return comments;
    }
    
    @Override
    public List<Comment> findRepliesByCommentId(Long parentCommentId) {
        log.debug("Finding replies for comment ID: {}", parentCommentId);
        List<Comment> replies = commentRepository.findByParentCommentId(parentCommentId);
        log.debug("Found {} replies for comment ID: {}", replies.size(), parentCommentId);
        return replies;
    }
    
    @Override
    public Comment replyToComment(Comment reply, Long parentCommentId, Long userId) {
        log.info("Creating reply to comment ID: {} by user ID: {}", parentCommentId, userId);
        
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> {
                    log.error("Parent comment not found with ID: {}", parentCommentId);
                    return new RuntimeException("Parent comment not found with id: " + parentCommentId);
                });
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new RuntimeException("User not found with id: " + userId);
                });
        
        reply.setParentComment(parentComment);
        reply.setPost(parentComment.getPost());
        reply.setUser(user);
        
        Comment savedReply = commentRepository.save(reply);
        log.info("Reply created successfully with ID: {}", savedReply.getId());
        return savedReply;
    }
}
