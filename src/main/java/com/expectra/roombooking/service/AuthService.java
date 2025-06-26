package com.expectra.roombooking.service;

import com.expectra.roombooking.dto.AuthResponseDTO;
import com.expectra.roombooking.model.User;
import com.expectra.roombooking.model.UserHotelRole;
import com.expectra.roombooking.repository.UserRepository;
import com.expectra.roombooking.repository.UserHotelRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserHotelRoleRepository userHotelRoleRepository;
    
    public AuthResponseDTO getUserInfoWithRoles(Long userId) {
        log.info("Getting user info with roles for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Check if user is active
        if (!user.getIsActive()) {
            log.warn("Inactive user attempting to access: {}", user.getUsername());
            throw new RuntimeException("Your account has been deactivated. Please contact the administrator.");
        }
        
        // Get user's active roles for all hotels
        List<UserHotelRole> userHotelRoles = userHotelRoleRepository.findByUserIdAndIsActiveTrue(user.getUserId());
        
        // Check if user has any active roles
        if (userHotelRoles.isEmpty()) {
            log.warn("User has no active roles: {}", user.getUsername());
            throw new RuntimeException("Your account has no active roles assigned. Please contact the administrator.");
        }
        
        log.info("User info retrieved successfully: {} with {} active roles", user.getUsername(), userHotelRoles.size());
        
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
} 