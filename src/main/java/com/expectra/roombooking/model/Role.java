package com.expectra.roombooking.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "roles")
@Data
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;
    
    @Column(name = "role_name", unique = true, nullable = false, length = 50)
    private String roleName;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    @JsonProperty("permissions")
    private Map<String, Object> permissions; // JSON object for permissions
    
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<UserHotelRole> userHotelRoles;
} 