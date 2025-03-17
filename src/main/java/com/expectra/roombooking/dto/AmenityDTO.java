package com.expectra.roombooking.dto;

import com.expectra.roombooking.model.AmenityType;
import lombok.Data;
import java.io.Serializable;

@Data
public class AmenityDTO implements Serializable {
    private Long amenityId;
    private Integer amenityCode;
    private AmenityType amenityType;
    private String amenityDescription;
}
