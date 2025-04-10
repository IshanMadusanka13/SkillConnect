package com.skillconnect.server.service.impl;

import com.skillconnect.server.model.Follow;
import com.skillconnect.server.model.User;
import com.skillconnect.server.repository.FollowRepository;
import com.skillconnect.server.repository.UserRepository;
import com.skillconnect.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, 
                          FollowRepository followRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.passwordEncoder = passwordEncoder;
        log.info("UserServiceImpl initialized");
    }

    @Override
    public User saveUser(User user) {
        log.info("Saving new user with email: {}", user.getEmail());
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        log.info("User saved successfully with ID: {}", savedUser.getId());
        return savedUser;
    }

    @Override
    public Optional<User> findById(Long userId) {
        log.debug("Finding user by ID: {}", userId);
        return userRepository.findById(userId);
    }

    @Override
    public User findByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
            log.debug("User found with email: {}", email);
            return user;
        } catch (RuntimeException e) {
            log.error("Failed to find user with email: {}", email, e);
            throw e;
        }
    }

    @Override
    public List<User> findAllUsers() {
        log.debug("Retrieving all users");
        List<User> users = userRepository.findAll();
        log.debug("Found {} users", users.size());
        return users;
    }

    @Override
    public User updateUser(User user) {
        log.info("Updating user with ID: {}", user.getId());
        // Check if user exists
        if (!userRepository.existsById(user.getId())) {
            log.error("User not found with ID: {}", user.getId());
            throw new RuntimeException("User not found with id: " + user.getId());
        }
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully: {}", user.getId());
        return updatedUser;
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Deleting user with ID: {}", userId);
        userRepository.deleteById(userId);
        log.info("User deleted successfully: {}", userId);
    }

    @Override
    public boolean existsByEmail(String email) {
        log.debug("Checking if user exists with email: {}", email);
        boolean exists = userRepository.existsByEmail(email);
        log.debug("User exists with email {}: {}", email, exists);
        return exists;
    }

    @Override
    public boolean existsByUsername(String username) {
        log.debug("Checking if user exists with username: {}", username);
        boolean exists = userRepository.existsByUsername(username);
        log.debug("User exists with username {}: {}", username, exists);
        return exists;
    }

    @Override
    public List<User> getFollowers(Long userId) {
        log.debug("Getting followers for user ID: {}", userId);
        // Get all follows where the target user is the specified user
        List<Follow> followers = followRepository.findByFollowedId(userId);
        
        // Extract the follower users from the follows
        List<User> followerUsers = followers.stream()
                .map(follow -> userRepository.findById(follow.getFollower().getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        
        log.debug("Found {} followers for user ID: {}", followerUsers.size(), userId);
        return followerUsers;
    }

    @Override
    public List<User> getFollowing(Long userId) {
        log.debug("Getting following for user ID: {}", userId);
        // Get all follows where the follower is the specified user
        List<Follow> following = followRepository.findByFollowerId(userId);
        
        // Extract the followed users from the follows
        List<User> followingUsers = following.stream()
                .map(follow -> userRepository.findById(follow.getFollowed().getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        
        log.debug("Found {} following for user ID: {}", followingUsers.size(), userId);
        return followingUsers;
    }

    @Override
    public User updateProfile(Long userId, String firstName, String lastName, String bio, String profileImage) {
        log.info("Updating profile for user ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new RuntimeException("User not found with id: " + userId);
                });
        
        if (firstName != null) {
            log.debug("Updating first name for user ID: {}", userId);
            user.setFirstName(firstName);
        }
        
        if (lastName != null) {
            log.debug("Updating last name for user ID: {}", userId);
            user.setLastName(lastName);
        }
        
        if (bio != null) {
            log.debug("Updating bio for user ID: {}", userId);
            user.setBio(bio);
        }
        
        if (profileImage != null) {
            log.debug("Updating profile image for user ID: {}", userId);
            user.setProfileImage(profileImage);
        }
        
        User updatedUser = userRepository.save(user);
        log.info("Profile updated successfully for user ID: {}", userId);
        return updatedUser;
    }

    @Override
    public boolean changePassword(Long userId, String currentPassword, String newPassword) {
        log.info("Attempting to change password for user ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new RuntimeException("User not found with id: " + userId);
                });
        
        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            log.warn("Password change failed for user ID: {} - Current password doesn't match", userId);
            return false;
        }
        
        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        log.info("Password changed successfully for user ID: {}", userId);
        return true;
    }
}