package com.expectra.roombooking.dto;

import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Media;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
public class RoomDTO implements Serializable {
    private Long roomId;
    private String roomNumber;
    private String roomType;
    private Integer floor;
    private BigDecimal price;
    private String roomName;
    private String roomDescription;
    private String roomBuildingName;
    private String roomBuildingCode;
    private String roomXCoordinates;
    private String roomYCoordinates;
    private Set<Amenity> amenities; // Relación con Amenity
    private Set<Media> medias = new HashSet<>();
}