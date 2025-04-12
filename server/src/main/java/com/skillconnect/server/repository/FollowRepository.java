package com.skillconnect.server.repository;

import com.skillconnect.server.model.Follow;
import com.skillconnect.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
    List<Follow> findByFollower_userid(int id);
    List<Follow> findByFollowing_userid(int id);
    
    int countByFollower(User follower);
    int countByFollowing(User following);
    
    Optional<Follow> findByFollower_UserIdAndFollowing_UserId(int follower, int following);
    boolean existsByFollowerAndFollowing(User follower, User following);
    
    void deleteByFollowerAndFollowing(User follower, User following);
}
