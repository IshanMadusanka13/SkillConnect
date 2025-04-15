package com.skillconnect.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.skillconnect.server.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    User findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
