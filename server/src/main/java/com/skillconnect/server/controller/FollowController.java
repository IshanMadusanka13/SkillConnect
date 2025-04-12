package com.skillconnect.server.controller;

import com.skillconnect.server.model.Follow;
import com.skillconnect.server.service.FollowService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follow")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class FollowController {

    private final FollowService followService;

    @PostMapping
    public ResponseEntity<Follow> followUser(@RequestBody Follow follow) {
        Follow created = followService.followUser(follow);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping
    public ResponseEntity<Void> unfollowUser(@RequestBody Follow follow) {
        followService.unfollowUser(follow);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<Integer> getFollowerCount(@PathVariable int userId) {
        return ResponseEntity.ok(followService.getFollowerCount(userId));
    }

    @GetMapping("/{userId}/following/count")
    public ResponseEntity<Integer> getFollowingCount(@PathVariable int userId) {
        return ResponseEntity.ok(followService.getFollowingCount(userId));
    }

    @GetMapping("/is-following")
    public ResponseEntity<Boolean> isFollowing(
            @RequestParam int followerId,
            @RequestParam int followingId) {
        return ResponseEntity.ok(followService.isFollowing(followerId, followingId));
    }
}

