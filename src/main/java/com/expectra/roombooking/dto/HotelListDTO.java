package com.expectra.roombooking.dto;

import com.expectra.roombooking.model.FloorPlan;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
// HotelListDTO.java
// ... (importaciones y @Data)
public class HotelListDTO implements Serializable {
    private Long hotelId;
    private String hotelCode;
    private String hotelChain;
    private String hotelBrand;
    private String hotelName;
    private String hotelStreet;
    private String hotelCity;
    private String hotelState;
    private String hotelCountry;
    private String contactName;
    private String contactTitle;
    private String hotelWebsiteUrl;
    private String hotelStatus;

    // Constructor para la proyección JPQL
    public HotelListDTO(Long hotelId, String hotelCode, String hotelName, String hotelWebsiteUrl, String hotelStatus,
                        String brandName, String chainName,
                        String hotelStreet, String city, String state, String country,
                        String contactFirstName, String contactLastName, String contactTitle) {
        this.hotelId = hotelId;
        this.hotelCode = hotelCode;
        this.hotelName = hotelName;
        this.hotelWebsiteUrl = hotelWebsiteUrl;
        this.hotelStatus = hotelStatus;
        this.hotelBrand = brandName;
        this.hotelChain = chainName;
        this.hotelStreet = hotelStreet;
        this.hotelCity = city;
        this.hotelState = state;
        this.hotelCountry = country;
        this.contactName = (contactFirstName != null ? contactFirstName : "") + (contactLastName != null ? " " + contactLastName : "");
        this.contactTitle = contactTitle;
    }

    // Constructor vacío si también lo necesitas
    public HotelListDTO() {}
}