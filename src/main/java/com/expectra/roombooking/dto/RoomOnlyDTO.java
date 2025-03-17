package com.expectra.roombooking.dto;

import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Media;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
public class RoomOnlyDTO implements Serializable {
    private Long roomId;
    private String roomNumber;
    private Integer floor;
    private BigDecimal price;
    private String roomName;
    private Set<Amenity> amenities; // Relación con Amenity
    private Set<Media> medias = new HashSet<>();
}