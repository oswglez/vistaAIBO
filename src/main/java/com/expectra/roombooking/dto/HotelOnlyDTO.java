package com.expectra.roombooking.dto;

import com.expectra.roombooking.model.FloorPlan;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

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