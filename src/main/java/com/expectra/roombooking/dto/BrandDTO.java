package com.expectra.roombooking.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class BrandDTO implements Serializable {
    private Long brandId;

    private String brandName;

    private String brandDescription;

    private Long chainId;

    private Set<HotelOnlyDTO> hotels;
}
