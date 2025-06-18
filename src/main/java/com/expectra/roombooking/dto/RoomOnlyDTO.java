package com.expectra.roombooking.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class RoomOnlyDTO implements Serializable {
    private Long roomId;
    private String roomNumber;
    private String roomType;
    private String roomName;
    private String roomDescription;
    private String roomBuildingName;
    private String roomBuildingCode;
    private String roomFloor;
    private String roomXCoordinates;
    private String roomYCoordinates;    
    private Double roomPrice;
}