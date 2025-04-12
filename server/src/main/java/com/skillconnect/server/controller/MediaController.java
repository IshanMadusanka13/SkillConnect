package com.skillconnect.server.controller;

import com.skillconnect.server.model.Media;
import com.skillconnect.server.service.MediaService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/media")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MediaController {

    private final MediaService mediaService;

    @PostMapping
    public ResponseEntity<Media> storeMedia(@RequestBody Media media) {
        Media stored = mediaService.storeMedia(media);
        return ResponseEntity.ok(stored);
    }

    @PostMapping("/{mediaId}/attach/{postId}")
    public ResponseEntity<Media> attachMediaToPost(@PathVariable int mediaId, @PathVariable int postId) {
        Media attached = mediaService.attachMediaToPost(mediaId, postId);
        return ResponseEntity.ok(attached);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Media>> getMediaByPostId(@PathVariable int postId) {
        List<Media> mediaList = mediaService.findMediaByPostId(postId);
        return ResponseEntity.ok(mediaList);
    }

    @GetMapping("/{mediaId}")
    public ResponseEntity<Media> getById(@PathVariable int mediaId) {
        return mediaService.findById(mediaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{mediaType}")
    public ResponseEntity<List<Media>> getByType(@PathVariable String mediaType) {
        List<Media> mediaList = mediaService.findMediaByType(mediaType);
        return ResponseEntity.ok(mediaList);
    }

    @DeleteMapping("/{mediaId}")
    public ResponseEntity<Void> deleteMedia(@PathVariable int mediaId) {
        mediaService.deleteMedia(mediaId);
        return ResponseEntity.noContent().build();
    }
}

