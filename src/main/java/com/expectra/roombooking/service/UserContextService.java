package com.expectra.roombooking.service;

import com.expectra.roombooking.dto.UserContextDTO;
import com.expectra.roombooking.model.Role;
import com.expectra.roombooking.repository.HotelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserContextService {
    
    @Autowired
    private AuthorizationService authorizationService;
    
    @Autowired
    private HotelRepository hotelRepository;
    
    public UserContextDTO getUserContext(Long userId, Long hotelId) {
        log.info("Getting user context for user: {} and hotel: {}", userId, hotelId);
        
        // Get user's active roles for all hotels
        List<Long> userHotelIds = authorizationService.getHotelIdsForUser(userId);
        
        if (userHotelIds.isEmpty()) {
            log.warn("User {} has no assigned hotels", userId);
            return UserContextDTO.builder()
                .userId(userId)
                .currentHotelId(null)
                .currentHotelName(null)
                .availableHotels(List.of())
                .currentRoles(List.of())
                .hasMultipleHotels(false)
                .build();
        }
        
        // If no hotel specified, use the first available hotel
        if (hotelId == null) {
            hotelId = userHotelIds.get(0);
            log.info("No hotel specified for user {}, using first available hotel: {}", userId, hotelId);
        }
        
        // Verify user has access to the specified hotel
        if (!userHotelIds.contains(hotelId)) {
            log.warn("User {} does not have access to hotel {}", userId, hotelId);
            throw new IllegalArgumentException("User does not have access to the specified hotel");
        }
        
        // Get hotel name
        String hotelName = hotelRepository.findById(hotelId)
            .map(hotel -> hotel.getHotelName())
            .orElse("Unknown Hotel");
        
        // Get user's roles for the current hotel
        List<Role> roles = authorizationService.getUserRolesForHotel(userId, hotelId);
        
        List<UserContextDTO.HotelRoleDTO> roleDTOs = roles.stream()
            .map(role -> UserContextDTO.HotelRoleDTO.builder()
                .roleId(role.getRoleId())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .permissions(role.getPermissions())
                .build())
            .collect(Collectors.toList());
        
        log.info("User {} has {} roles for hotel {}", userId, roleDTOs.size(), hotelId);
        
        return UserContextDTO.builder()
            .userId(userId)
            .currentHotelId(hotelId)
            .currentHotelName(hotelName)
            .availableHotels(userHotelIds)
            .currentRoles(roleDTOs)
            .hasMultipleHotels(userHotelIds.size() > 1)
            .build();
    }
    
    public boolean userHasRole(Long userId, Long hotelId, String roleName) {
        return authorizationService.hasRole(userId, hotelId, roleName);
    }
    
    public boolean userHasPermission(Long userId, Long hotelId, String permission) {
        return authorizationService.hasPermission(userId, hotelId, permission);
    }
} 