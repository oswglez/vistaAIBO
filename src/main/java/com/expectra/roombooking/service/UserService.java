package com.expectra.roombooking.service;

import com.expectra.roombooking.dto.AuthResponseDTO;
import com.expectra.roombooking.dto.LoginRequestDTO;
import com.expectra.roombooking.model.User;
import com.expectra.roombooking.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponseDTO authenticateUser(LoginRequestDTO loginRequest) {
        log.info("Starting authentication for user: {}", loginRequest.getUsername());
        
        try {
            // Buscar usuario por username
            log.debug("Searching for user with username: {}", loginRequest.getUsername());
            Optional<User> userOpt = userRepository.findByUsernameAndIsActiveTrue(loginRequest.getUsername());
            
            if (userOpt.isEmpty()) {
                log.warn("User not found or inactive: {}", loginRequest.getUsername());
                throw new RuntimeException("Invalid credentials - User not found");
            }
            
            User user = userOpt.get();
            log.debug("User found: {} (ID: {})", user.getUsername(), user.getUserId());
            
            // Verificar contraseña
            log.debug("Verifying password for user: {}", user.getUsername());
            log.debug("Stored password hash: {}", user.getPasswordHash());
            log.debug("Input password: {}", loginRequest.getPassword());
            
            boolean passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash());
            log.debug("Password matches: {}", passwordMatches);
            
            if (!passwordMatches) {
                log.warn("Invalid password for user: {}", user.getUsername());
                throw new RuntimeException("Invalid credentials - Wrong password");
            }
            
            log.info("Password verified successfully for user: {}", user.getUsername());
            
            // Actualizar último login
            log.debug("Updating last login for user: {}", user.getUsername());
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
            
            // Crear respuesta
            log.debug("Creating authentication response for user: {}", user.getUsername());
            AuthResponseDTO response = new AuthResponseDTO();
            response.setToken("dummy-token-" + System.currentTimeMillis()); // En producción, usar JWT
            response.setRefreshToken("dummy-refresh-token");
            response.setExpiresAt(LocalDateTime.now().plusHours(24));
            
            AuthResponseDTO.UserDTO userDTO = new AuthResponseDTO.UserDTO();
            userDTO.setUserId(user.getUserId());
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setIsActive(user.getIsActive());
            
            response.setUser(userDTO);
            
            log.info("Authentication successful for user: {}", user.getUsername());
            return response;
            
        } catch (Exception e) {
            log.error("Authentication failed for user: {}. Error: {}", loginRequest.getUsername(), e.getMessage(), e);
            throw e;
        }
    }

    public User getUserById(Long userId) {
        log.debug("Getting user by ID: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", userId);
                    return new RuntimeException("User not found");
                });
    }

    public void updateLastLogin(Long userId) {
        log.debug("Updating last login for user ID: {}", userId);
        User user = getUserById(userId);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        log.debug("Last login updated for user ID: {}", userId);
    }
} 