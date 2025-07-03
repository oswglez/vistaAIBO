package com.expectra.roombooking.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserHotelRoleDTO {
    private Long userHotelRoleId;
    private Long userId;
    private String username;
    private Long hotelId;
    private String hotelName;
    private Long brandId;
    private String brandName;
    private Long chainId;
    private String chainName;
    private Long roleId;
    private String roleName;
    private Long assignedById;
    private String assignedByUsername;
    private LocalDateTime assignedAt;
    private Boolean isActive;

    // Getters y setters
    public Long getUserHotelRoleId() { return userHotelRoleId; }
    public void setUserHotelRoleId(Long userHotelRoleId) { this.userHotelRoleId = userHotelRoleId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Long getHotelId() { return hotelId; }
    public void setHotelId(Long hotelId) { this.hotelId = hotelId; }
    public String getHotelName() { return hotelName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }
    public Long getBrandId() { return brandId; }
    public void setBrandId(Long brandId) { this.brandId = brandId; }
    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }
    public Long getChainId() { return chainId; }
    public void setChainId(Long chainId) { this.chainId = chainId; }
    public String getChainName() { return chainName; }
    public void setChainName(String chainName) { this.chainName = chainName; }
    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    public Long getAssignedById() { return assignedById; }
    public void setAssignedById(Long assignedById) { this.assignedById = assignedById; }
    public String getAssignedByUsername() { return assignedByUsername; }
    public void setAssignedByUsername(String assignedByUsername) { this.assignedByUsername = assignedByUsername; }
    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
} 