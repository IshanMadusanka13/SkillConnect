package com.skillconnect.server.service;

import com.skillconnect.server.model.Comment;
import com.skillconnect.server.model.Post;
import com.skillconnect.server.model.User;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    
    Comment saveComment(Comment comment);
    
    Optional<Comment> findById(Long commentId);
    
    List<Comment> findCommentsByPost(Post post);
    
    List<Comment> findCommentsByPostId(Long postId);
    
    List<Comment> findCommentsByUser(User user);
    
    Comment updateComment(Comment comment);
    
    void deleteComment(Long commentId);
    
    long getCommentCount(Long postId);
    
    boolean isCommentOwner(Long commentId, Long userId);
}
