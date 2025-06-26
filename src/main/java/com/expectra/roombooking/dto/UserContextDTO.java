package com.expectra.roombooking.dto;

import lombok.Data;
import lombok.Builder;

import java.util.List;

@Data
@Builder
public class UserContextDTO {
    
    private Long userId;
    private Long currentHotelId;
    private String currentHotelName;
    private List<Long> availableHotels;
    private List<HotelRoleDTO> currentRoles;
    private boolean hasMultipleHotels;
    
    @Data
    @Builder
    public static class HotelRoleDTO {
        private Long roleId;
        private String roleName;
        private String description;
        private String permissions;
    }
} 