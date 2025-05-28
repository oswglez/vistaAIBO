package com.expectra.roombooking.dto;

import com.expectra.roombooking.model.AmenityTypes;
import lombok.Data;
import java.io.Serializable;

@Data
public class AmenityDTO implements Serializable {
    private Long amenityId;
    private String amenityCode;
    private String amenityType;
    private String amenityDescription;
}
