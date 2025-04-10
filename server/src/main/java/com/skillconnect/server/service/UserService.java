package com.skillconnect.server.service;

import com.skillconnect.server.model.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    
    User saveUser(User user);
    Optional<User> findById(Long userId);
    User findByEmail(String email);
    List<User> findAllUsers();
    User updateUser(User user);
    void deleteUser(Long userId);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    List<User> getFollowers(Long userId);
    List<User> getFollowing(Long userId);
    User updateProfile(Long userId, String firstName, String lastName, String bio, String profileImage);
    boolean changePassword(Long userId, String currentPassword, String newPassword);
}
