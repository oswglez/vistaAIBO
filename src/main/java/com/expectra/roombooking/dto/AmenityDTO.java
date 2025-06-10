package com.expectra.roombooking.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class AmenityDTO implements Serializable {
    private Long amenityId;
    private String amenityCode;
    private String amenityType;
    private String amenityDescription;
}
