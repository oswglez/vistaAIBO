package com.expectra.roombooking.service;

import com.expectra.roombooking.dto.LoginRequestDTO;
import com.expectra.roombooking.dto.AuthResponseDTO;
import com.expectra.roombooking.exception.AuthenticationException;
import com.expectra.roombooking.model.User;
import com.expectra.roombooking.model.UserHotelRole;
import com.expectra.roombooking.repository.UserRepository;
import com.expectra.roombooking.repository.UserHotelRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserHotelRoleRepository userHotelRoleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public AuthResponseDTO authenticate(LoginRequestDTO loginRequest) {
        log.info("Attempting to authenticate user: {}", loginRequest.getUsername());
        
        // Find user by username
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        
        if (userOpt.isEmpty()) {
            log.warn("User not found: {}", loginRequest.getUsername());
            throw new AuthenticationException(
                "Invalid credentials", 
                "INVALID_CREDENTIALS"
            );
        }
        
        User user = userOpt.get();
        
        // Check if user is active
        if (!user.getIsActive()) {
            log.warn("Inactive user attempting to authenticate: {}", loginRequest.getUsername());
            throw new AuthenticationException(
                "Your account has been deactivated. Please contact the administrator.", 
                "ACCOUNT_DISABLED"
            );
        }
        
        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            log.warn("Incorrect password for user: {}", loginRequest.getUsername());
            throw new AuthenticationException(
                "Invalid credentials", 
                "INVALID_CREDENTIALS"
            );
        }
        
        // Get user's active roles for all hotels
        List<UserHotelRole> userHotelRoles = userHotelRoleRepository.findByUserIdAndIsActiveTrue(user.getUserId());
        
        // Check if user has any active roles
        if (userHotelRoles.isEmpty()) {
            log.warn("User has no active roles: {}", loginRequest.getUsername());
            throw new AuthenticationException(
                "Your account has no active roles assigned. Please contact the administrator.", 
                "NO_ROLES_ASSIGNED"
            );
        }
        
        log.info("User authenticated successfully: {} with {} active roles", loginRequest.getUsername(), userHotelRoles.size());
        
        // Create response with correct structure
        AuthResponseDTO response = new AuthResponseDTO();
        AuthResponseDTO.UserDTO userDTO = new AuthResponseDTO.UserDTO();
        
        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setIsActive(user.getIsActive());
        
        response.setUser(userDTO);
        response.setToken("dummy-token-" + System.currentTimeMillis()); // Placeholder
        response.setRefreshToken("dummy-refresh-" + System.currentTimeMillis()); // Placeholder
        response.setExpiresAt(java.time.LocalDateTime.now().plusHours(24)); // Placeholder
        
        // Map user hotel roles to DTOs
        List<AuthResponseDTO.UserHotelRoleDTO> userHotelRoleDTOs = userHotelRoles.stream()
            .map(this::mapToUserHotelRoleDTO)
            .collect(Collectors.toList());
        
        response.setUserHotelRoles(userHotelRoleDTOs);
        
        return response;
    }
    
    private AuthResponseDTO.UserHotelRoleDTO mapToUserHotelRoleDTO(UserHotelRole userHotelRole) {
        AuthResponseDTO.UserHotelRoleDTO dto = new AuthResponseDTO.UserHotelRoleDTO();
        
        dto.setUserHotelRoleId(userHotelRole.getUserHotelRoleId());
        dto.setHotelId(userHotelRole.getHotel().getHotelId());
        dto.setHotelName(userHotelRole.getHotel().getHotelName());
        dto.setRoleId(userHotelRole.getRole().getRoleId());
        dto.setRoleName(userHotelRole.getRole().getRoleName());
        dto.setRoleDescription(userHotelRole.getRole().getDescription());
        dto.setPermissions(userHotelRole.getRole().getPermissions());
        dto.setIsActive(userHotelRole.getIsActive());
        dto.setAssignedAt(userHotelRole.getAssignedAt());
        
        return dto;
    }
    
    public String generatePasswordHash(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
} 