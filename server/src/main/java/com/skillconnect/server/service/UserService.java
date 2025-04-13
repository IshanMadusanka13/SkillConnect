package com.skillconnect.server.service;

import com.skillconnect.server.model.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    
    User saveUser(User user);
    Optional<User> findById(int userId);
    User findByEmail(String email);
    List<User> findAllUsers();
    User updateUser(int userid, User user);
    void deleteUser(int userId);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean changePassword(int userId, String currentPassword, String newPassword);
}
