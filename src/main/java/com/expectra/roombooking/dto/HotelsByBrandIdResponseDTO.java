package com.expectra.roombooking.dto;

import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.model.Room;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class HotelsByBrandIdResponseDTO {
    private String name;
    private Integer roomQty;
    private List<Hotel> hotels;
}
