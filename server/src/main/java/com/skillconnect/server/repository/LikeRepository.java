package com.skillconnect.server.repository;

import com.skillconnect.server.model.Like;
import com.skillconnect.server.model.Post;
import com.skillconnect.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, int> {
    List<Like> findByPost(Post post);
    List<Like> findByUser(User user);
    Optional<Like> findByUserAndPost(User user, Post post);
    int countByPost(Post post);
    boolean existsByUserAndPost(User user, Post post);
}
