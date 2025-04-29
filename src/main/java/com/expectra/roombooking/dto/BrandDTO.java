package com.expectra.roombooking.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class BrandDTO implements Serializable {
    private Long brandId;

    private String brandName;

    private String brandDescription;
}
