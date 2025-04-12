package com.skillconnect.server.service.serviceImpl;

import com.skillconnect.server.model.Media;
import com.skillconnect.server.model.Post;
import com.skillconnect.server.repository.MediaRepository;
import com.skillconnect.server.repository.PostRepository;
import com.skillconnect.server.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@Transactional
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final PostRepository postRepository;
    
    @Autowired
    public MediaServiceImpl(
            MediaRepository mediaRepository,
            PostRepository postRepository) {
        this.mediaRepository = mediaRepository;
        this.postRepository = postRepository;
        
        
        log.info("MediaServiceImpl initialized");
    }
    
    @Override
    public Media storeMedia(Media media) {
        log.info("Storing media for post ID: {}, media type: {}", media.getPost().getPostId(), media.getMediaType());
    
        postRepository.findByPostId(media.getPost().getPostId())
                .orElseThrow(() -> {
                    log.error("Post not found with ID: {}", media.getPost().getPostId());
                    return new RuntimeException("Post not found with id: " + media.getPost().getPostId());
                });
    
                Media savedMedia = mediaRepository.save(media);
                log.info("Media stored successfully with ID: {}", savedMedia.getMediaId());
                return savedMedia;
    }
    
    
    @Override
    public Media attachMediaToPost(int mediaId, int postId) {
        log.info("Attaching media ID: {} to post ID: {}", mediaId, postId);
        
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> {
                    log.error("Media not found with ID: {}", mediaId);
                    return new RuntimeException("Media not found with id: " + mediaId);
                });
        
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> {
                    log.error("Post not found with ID: {}", postId);
                    return new RuntimeException("Post not found with id: " + postId);
                });
        
        media.setPost(post);
        Media updatedMedia = mediaRepository.save(media);
        log.info("Media attached successfully to post ID: {}", postId);
        return updatedMedia;
    }
    
    @Override
    public Optional<Media> findById(int mediaId) {
        log.debug("Finding media by ID: {}", mediaId);
        return mediaRepository.findById(mediaId);
    }
    
    @Override
    public List<Media> findMediaByPostId(int postId) {
        log.debug("Finding media for post ID: {}", postId);
        List<Media> mediaList = mediaRepository.findByPostId(postId);
        log.debug("Found {} media items for post ID: {}", mediaList.size(), postId);
        return mediaList;
    }
    
    @Override
    public void deleteMedia(int mediaId) {
        log.info("Deleting media with ID: {}", mediaId);
        
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> {
                    log.error("Media not found with ID: {}", mediaId);
                    return new RuntimeException("Media not found with id: " + mediaId);
                });
        
        // Delete the file from storage
        try {
            Path filePath = Paths.get(media.getFilePath());
            Files.deleteIfExists(filePath);
            log.debug("Deleted file from storage: {}", media.getFilePath());
        } catch (IOException ex) {
            log.error("Error deleting file: {}", media.getFilePath(), ex);
            // Continue with deleting the database record even if file deletion fails
        }
        
        // Delete the database record
        mediaRepository.delete(media);
        log.info("Media deleted successfully: {}", mediaId);
    }
    
    @Override
    public List<Media> findMediaByType(String mediaType) {
        log.debug("Finding media by type: {}", mediaType);
        List<Media> mediaList = mediaRepository.findByMediaType(mediaType);
        log.debug("Found {} media items of type: {}", mediaList.size(), mediaType);
        return mediaList;
    } 
}
