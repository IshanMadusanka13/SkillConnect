package com.skillconnect.server.repository;

import com.skillconnect.server.model.Follow;
import com.skillconnect.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, int> {
    List<Follow> findByFollower(User follower);
    List<Follow> findByFollowing(User following);
    
    int countByFollower(User follower);
    int countByFollowing(User following);
    
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
    boolean existsByFollowerAndFollowing(User follower, User following);
    
    void deleteByFollowerAndFollowing(User follower, User following);
}
