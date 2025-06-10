package com.expectra.roombooking.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class HotelOnlyDTO implements Serializable {
    private Long hotelId;
    private String hotelName;
    private String hotelCode;
    private String localPhone;
    private String celularPhone;
    private String pmsVendor;
    private Long pmsHotelId;
    private String pmsToken; //
    private String crsVendor;
    private Long crsHotelId;
    private String crsToken; //
    private String disclaimer;
    private Integer totalFloors;
    private Integer totalRooms;
    private String hotelWebsiteUrl;
    private Boolean hotelDeleted;
}