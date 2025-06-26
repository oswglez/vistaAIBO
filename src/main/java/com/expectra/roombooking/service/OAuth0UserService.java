package com.expectra.roombooking.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.expectra.roombooking.model.User;
import com.expectra.roombooking.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class OAuth0UserService {

    private final UserRepository userRepository;

    public OAuth0UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getOrCreateUserFromJwt(DecodedJWT jwt) {
        String auth0UserId = jwt.getSubject();
        String email = jwt.getClaim("email").asString();
        String name = jwt.getClaim("name").asString();
        
        log.info("Processing OAuth0 user - ID: {}, Email: {}, Name: {}", auth0UserId, email, name);
        
        // Try to find user by Auth0 ID first
        Optional<User> existingUser = userRepository.findByAuth0Id(auth0UserId);
        
        if (existingUser.isPresent()) {
            log.info("Found existing user with Auth0 ID: {}", auth0UserId);
            return existingUser.get();
        }
        
        // Try to find user by email
        if (email != null) {
            existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent()) {
                log.info("Found existing user with email: {}, updating Auth0 ID", email);
                User user = existingUser.get();
                user.setAuth0Id(auth0UserId);
                return userRepository.save(user);
            }
        }
        
        // Create new user
        log.info("Creating new user for Auth0 ID: {}", auth0UserId);
        User newUser = new User();
        newUser.setAuth0Id(auth0UserId);
        newUser.setEmail(email);
        newUser.setUsername(email != null ? email.split("@")[0] : auth0UserId);
        newUser.setFirstName(name != null ? name.split(" ")[0] : "Unknown");
        newUser.setLastName(name != null && name.split(" ").length > 1 ? name.split(" ")[1] : "");
        newUser.setIsActive(true);
        
        return userRepository.save(newUser);
    }

    public Optional<User> getUserByAuth0Id(String auth0Id) {
        return userRepository.findByAuth0Id(auth0Id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
} 