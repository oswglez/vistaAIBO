package com.expectra.roombooking.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserResponseDTO implements Serializable {
    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private String auth0Id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;

    public UserResponseDTO() {}

    public UserResponseDTO(Long userId, String username, String email, String firstName, String lastName, Boolean isActive, String auth0Id, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime lastLogin) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.auth0Id = auth0Id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastLogin = lastLogin;
    }
} 