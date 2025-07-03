package com.expectra.roombooking.dto;

import lombok.Data;
import java.util.Map;

@Data
public class RoleDTO {
    private Long roleId;
    private String roleName;
    private String description;
    private Map<String, Object> permissions;

    // Getters y setters
    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
    
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Map<String, Object> getPermissions() { return permissions; }
    public void setPermissions(Map<String, Object> permissions) { this.permissions = permissions; }
} 