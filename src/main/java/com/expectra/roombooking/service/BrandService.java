package com.expectra.roombooking.service;

import com.expectra.roombooking.dto.BrandDTO;

import com.expectra.roombooking.model.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandService {
    // Operaciones básicas CRUD
    Brand createBrand(Brand brand);
    Optional<BrandDTO> getBrandById(Long brandId);
    List<BrandDTO> getAllBrands();
    BrandDTO updateBrand(Long brandId, BrandDTO brandDetails);
    void deleteBrand(Long brandId);
    BrandDTO getAllHotelsByBrandId(Long brandId);
}