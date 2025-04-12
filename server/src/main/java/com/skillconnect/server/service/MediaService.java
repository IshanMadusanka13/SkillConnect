package com.skillconnect.server.service;

import com.skillconnect.server.model.Media;

import java.util.List;
import java.util.Optional;

public interface MediaService {

    Media storeMedia(Media media);

    Media attachMediaToPost(int mediaId, int postId);

    List<Media> findMediaByPostId(int postId);

    Optional<Media> findById(int mediaId);
    
    List<Media> findMediaByType(String mediaType);
    
    void deleteMedia(int mediaId);
}
