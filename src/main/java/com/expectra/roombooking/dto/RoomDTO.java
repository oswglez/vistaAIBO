package com.expectra.roombooking.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class RoomDTO implements Serializable {
    private Long roomId;
    private String roomNumber;
    private String roomType;
    private String roomName;
    private String roomBuilding;
    private String roomFloor;
    private BigDecimal price;
}