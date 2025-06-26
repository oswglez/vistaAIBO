package com.expectra.roombooking.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AuthResponseDTO {
    
    private String token;
    private String refreshToken;
    private LocalDateTime expiresAt;
    private UserDTO user;
    private List<UserHotelRoleDTO> userHotelRoles;
    
    @Data
    public static class UserDTO {
        private Long userId;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private Boolean isActive;
    }
    
    @Data
    public static class UserHotelRoleDTO {
        private Long userHotelRoleId;
        private Long hotelId;
        private String hotelName;
        private Long roleId;
        private String roleName;
        private String roleDescription;
        private String permissions;
        private Boolean isActive;
        private LocalDateTime assignedAt;
    }
} 