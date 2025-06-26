package com.expectra.roombooking.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_hotel_roles")
@Data
public class UserHotelRole {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_hotel_role_id")
    private Long userHotelRoleId;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
    
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    
    @ManyToOne
    @JoinColumn(name = "assigned_by")
    private User assignedBy;
    
    @CreationTimestamp
    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
} 