package com.expectra.roombooking.service.impl;

import com.expectra.roombooking.dto.BrandDTO;
import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Brand;
import com.expectra.roombooking.repository.BrandRepository;
import com.expectra.roombooking.service.BrandService;
import com.expectra.roombooking.service.BrandValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandValidationService brandValidator;

    @Autowired
    public BrandServiceImpl(BrandRepository brandRepository, BrandValidationService brandValidator) {
        this.brandRepository = brandRepository;
        this.brandValidator = brandValidator;
    }

    @Override
    @Transactional
    public Brand createBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    @Transactional
    public Optional<BrandDTO> getBrandById(Long brandId) {
        // Obtener la entidad Brand del repositorio
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + brandId));

        // Mapear la entidad Brand a un DTO
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
}