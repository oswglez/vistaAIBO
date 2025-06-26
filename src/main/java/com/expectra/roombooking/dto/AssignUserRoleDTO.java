package com.expectra.roombooking.dto;

import lombok.Data;

@Data
public class AssignUserRoleDTO {
    private Long userId;
    private Long hotelId;
    private Long roleId;
    private Long assignedBy; // Optional, for audit
}