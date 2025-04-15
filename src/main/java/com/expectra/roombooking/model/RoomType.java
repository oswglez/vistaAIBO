package com.expectra.roombooking.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="room_type")
@Data
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_type_id")
    private Long roomTypeId;

    @Column(name = "media_name", nullable = false)
    private String roomTypeName;
}