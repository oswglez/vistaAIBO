package com.expectra.roombooking.dto;

import lombok.Data;
import java.util.Set;

@Data
public class ChainDTO {
    private Long chainId;
    private String chainName;
    private String chainDescription;
    private Set<BrandDTO> brands;
}
