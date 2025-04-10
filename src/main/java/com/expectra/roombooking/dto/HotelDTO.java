package com.expectra.roombooking.dto;

import com.expectra.roombooking.model.FloorPlan;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Data
public class HotelDTO implements Serializable {
    private Long hotelId;
    private String hotelName;
    private String localPhone;
    private String celularPhone;
    private String pmsVendor;
    private Long pmsHotelId;
    private String pmsToken; //
    private String crsVendor;
    private Long crsHotelId;
    private String crsToken; //
    private String disclaimer;
    private List<FloorPlan> floorPlans;
    // Colección de habitaciones asociadas
    private Set<RoomDTO> rooms;
}
