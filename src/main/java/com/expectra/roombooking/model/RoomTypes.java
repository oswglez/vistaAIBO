package com.expectra.roombooking.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="room_types")
@Data
public class RoomTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_types_id")
    private Long roomTypeId;

    @Column(name = "room_types_name", nullable = false)
    private String roomTypeName;

    @Column(name="room_types_description", nullable=false)
    private String roomTypeDescription;
}