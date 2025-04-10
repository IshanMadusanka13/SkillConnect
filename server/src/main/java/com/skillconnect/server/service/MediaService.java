package com.skillconnect.server.service;

import com.skillconnect.server.model.Media;
import com.skillconnect.server.model.Post;

import java.util.List;
import java.util.Optional;

public interface MediaService {
    
    Media saveMedia(Media media);
    
    Optional<Media> findById(Long mediaId);
    
    List<Media> findMediaByPost(Post post);
    
    List<Media> findMediaByPostId(Long postId);
    
    List<Media> findMediaByType(String mediaType);
    
    void deleteMedia(Long mediaId);
    
    void deleteMediaByPost(Long postId);
}
