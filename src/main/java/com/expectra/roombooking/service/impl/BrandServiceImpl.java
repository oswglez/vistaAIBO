package com.expectra.roombooking.service.impl;

import com.expectra.roombooking.dto.BrandDTO;
import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Brand;
import com.expectra.roombooking.repository.BrandRepository;
import com.expectra.roombooking.service.BrandService;
import com.expectra.roombooking.service.BrandValidationService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService {
    private static final Logger log = LoggerFactory.getLogger(BrandServiceImpl.class);

    private final BrandRepository brandRepository;
    private final BrandValidationService brandValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public BrandServiceImpl(BrandRepository brandRepository, BrandValidationService brandValidator, ModelMapper modelMapper) {
        this.brandRepository = brandRepository;
        this.brandValidator = brandValidator;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public Brand createBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    @Transactional
    public Optional<BrandDTO> getBrandById(Long brandId) {
        // Get Brand entity from repository
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + brandId));

        // Map Brand entity to DTO
        BrandDTO dto = new BrandDTO();
        dto.setBrandId(brand.getBrandId());
        dto.setBrandName(brand.getBrandName());
        dto.setBrandDescription(brand.getBrandDescription());
        return Optional.of(dto);
    }

    @Override
    public List<BrandDTO> getAllBrands() {
        List<Brand> brands = (List<Brand>) brandRepository.findAll();
        List<BrandDTO> brandDTOs = new ArrayList<>();
        for (Brand brand : brands) {
            BrandDTO dto = new BrandDTO();
            dto.setBrandId(brand.getBrandId());
            dto.setBrandName(brand.getBrandName());
            dto.setBrandDescription(brand.getBrandDescription());

            brandDTOs.add(dto);
        }
        return brandDTOs;
    }

    @Override
    @Transactional
    public BrandDTO updateBrand(Long brandId, BrandDTO brandsDetails) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + brandId));

        if (brandsDetails.getBrandDescription() != null) {
            brand.setBrandDescription(brandsDetails.getBrandDescription());
        }
        brandRepository.save(brand);
        brandsDetails.setBrandId(brand.getBrandId());
        return brandsDetails;
    }

    @Override
    @Transactional
    public void deleteBrand(Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + brandId));
        brandRepository.delete(brand);
    }

    @Override
    public BrandDTO getAllHotelsByBrandId(Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + brandId));
        
        BrandDTO dto = new BrandDTO();
        dto.setBrandId(brand.getBrandId());
        dto.setBrandName(brand.getBrandName());
        dto.setBrandDescription(brand.getBrandDescription());
        return dto;
    }
}