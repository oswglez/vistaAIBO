package com.expectra.roombooking.service;

import com.expectra.roombooking.model.User;
import com.expectra.roombooking.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserById(Long userId) {
        log.debug("Getting user by ID: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", userId);
                    return new RuntimeException("User not found");
                });
    }

    public User getUserByAuth0Id(String auth0Id) {
        log.debug("Getting user by Auth0 ID: {}", auth0Id);
        return userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> {
                    log.warn("User not found with Auth0 ID: {}", auth0Id);
                    return new RuntimeException("User not found");
                });
    }

    public User getUserByEmail(String email) {
        log.debug("Getting user by email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found with email: {}", email);
                    return new RuntimeException("User not found");
                });
    }

    public Optional<User> findUserByAuth0Id(String auth0Id) {
        log.debug("Finding user by Auth0 ID: {}", auth0Id);
        return userRepository.findByAuth0Id(auth0Id);
    }

    public Optional<User> findUserByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        log.debug("Saving user: {}", user.getUsername());
        return userRepository.save(user);
    }

    public void updateLastLogin(Long userId) {
        log.debug("Updating last login for user ID: {}", userId);
        User user = getUserById(userId);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        log.debug("Last login updated for user ID: {}", userId);
    }

    public void updateLastLoginByAuth0Id(String auth0Id) {
        log.debug("Updating last login for Auth0 ID: {}", auth0Id);
        User user = getUserByAuth0Id(auth0Id);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        log.debug("Last login updated for Auth0 ID: {}", auth0Id);
    }
} 