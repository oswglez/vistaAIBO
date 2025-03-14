package com.expectra.roombooking.dto;

import lombok.Data;

import java.io.Serializable;
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
    // Colección de habitaciones asociadas
    private Set<RoomDTO> rooms;
}
