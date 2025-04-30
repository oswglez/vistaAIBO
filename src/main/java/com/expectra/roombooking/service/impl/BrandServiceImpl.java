package com.expectra.roombooking.service.impl;

import com.expectra.roombooking.dto.BrandDTO;
import com.expectra.roombooking.dto.HotelOnlyDTO;
import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Brand;
import com.expectra.roombooking.repository.BrandRepository;
import com.expectra.roombooking.service.BrandService;
import com.expectra.roombooking.service.BrandValidationService;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    @Transactional(readOnly = true)
    public BrandDTO getAllHotelsByBrandId(Long brandId) {
        log.debug("Attempting fetch using findByIdWithHotels for brandId: {}", brandId);
        // *** CALL THIS METHOD ***
        Brand brand = brandRepository.findByIdWithHotels(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + brandId));

        // Remove the Hibernate.initialize() block now

        // Log size after fetch
        int hotelsSize = (brand.getHotels() != null) ? brand.getHotels().size() : -1;
        log.debug("Fetched brand via JOIN FETCH. Hotels collection size: {}", hotelsSize);

        // ... rest of the mapping logic ...
        BrandDTO dto = modelMapper.map(brand, BrandDTO.class);
        Set<HotelOnlyDTO> hotelDTOs = brand.getHotels().stream()
                .map(hotel -> modelMapper.map(hotel, HotelOnlyDTO.class))
                .collect(Collectors.toSet());
        dto.setHotels(hotelDTOs);

        return dto;
    }
}