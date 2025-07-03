package com.expectra.roombooking.service;

import com.expectra.roombooking.model.Role;
import com.expectra.roombooking.model.UserHotelRole;
import com.expectra.roombooking.repository.UserHotelRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthorizationService {
    
    @Autowired
    private UserHotelRoleRepository userHotelRoleRepository;
    
    public boolean hasPermission(Long userId, Long hotelId, String permission) {
        List<UserHotelRole> userRoles = userHotelRoleRepository
            .findByUserIdAndHotelIdAndIsActiveTrue(userId, hotelId);
        
        return userRoles.stream()
            .anyMatch(userRole -> {
                Map<String, Object> permissions = userRole.getRole().getPermissions();
                // Check if the permission exists and is true
                return permissions != null && 
                       permissions.containsKey(permission) && 
                       Boolean.TRUE.equals(permissions.get(permission));
            });
    }
    
    public List<Role> getUserRolesForHotel(Long userId, Long hotelId) {
        return userHotelRoleRepository
            .findByUserIdAndHotelIdAndIsActiveTrue(userId, hotelId)
            .stream()
            .map(UserHotelRole::getRole)
            .collect(Collectors.toList());
    }
    
    public List<Long> getHotelIdsForUser(Long userId) {
        return userHotelRoleRepository
            .findByUserIdAndIsActiveTrue(userId)
            .stream()
            .map(userRole -> userRole.getHotel().getHotelId())
            .distinct()
            .collect(Collectors.toList());
    }
    
    public boolean hasRole(Long userId, Long hotelId, String roleName) {
        List<UserHotelRole> userRoles = userHotelRoleRepository
            .findByUserIdAndHotelIdAndIsActiveTrue(userId, hotelId);
        
        return userRoles.stream()
            .anyMatch(userRole -> roleName.equals(userRole.getRole().getRoleName()));
    }
} 