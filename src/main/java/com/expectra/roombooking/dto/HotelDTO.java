package com.expectra.roombooking.dto;

import com.expectra.roombooking.model.FloorPlan;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
@Data
public class HotelDTO implements Serializable {
    private Long hotelId;
    private String hotelName;
    private String hotelCode;
    private String localPhone;
    private String celularPhone;
    private String pmsVendor;
    private Long pmsHotelId;
    private String pmsToken;
    private String crsVendor;
    private Long crsHotelId;
    private String crsToken;
    private String disclaimer;
    private Integer totalFloors;
    private Integer totalRooms;
    private String hotelWebsiteUrl;
    private Boolean hotelDeleted;
    private List<FloorPlan> floorPlans;
    // Colección de habitaciones asociadas
    private Set<RoomOnlyDTO> rooms;
}