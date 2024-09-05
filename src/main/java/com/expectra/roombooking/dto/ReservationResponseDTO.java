package com.expectra.roombooking.dto;

import lombok.Data;

@Data
public class ReservationResponseDTO {
    String arrivalDate;
    String departureDate;
    String arrivalTime;
    String departureTime;
    String roomType;

}
