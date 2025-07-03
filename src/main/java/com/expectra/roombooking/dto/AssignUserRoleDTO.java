package com.expectra.roombooking.dto;

import lombok.Data;

@Data
public class AssignUserRoleDTO {
    private Long userId;
    private Long hotelId;   // nullable
    private Long brandId;   // nullable
    private Long chainId;   // nullable
    private Long roleId;
    private Long assignedBy; // Optional, for audit
    private Boolean isActive;
}