package com.expectra.roombooking.service.impl;

import com.expectra.roombooking.dto.BrandDTO;
import com.expectra.roombooking.dto.HotelDTO;
import com.expectra.roombooking.dto.HotelOnlyDTO;
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
import java.util.Collections;
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
        log.info("Entering createBrand() with brand: {}", brand);
        Brand saved = brandRepository.save(brand);
        log.info("Brand created with ID: {}", saved.getBrandId());
        return saved;
    }

    @Override
    @Transactional
    public Optional<BrandDTO> getBrandById(Long brandId) {
        log.info("Entering getBrandById() with brandId: {}", brandId);
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + brandId));
        log.debug("Found brand: {}", brand);
        BrandDTO dto = new BrandDTO();
        dto.setBrandId(brand.getBrandId());
        dto.setBrandName(brand.getBrandName());
        dto.setBrandDescription(brand.getBrandDescription());
        log.info("Returning BrandDTO for brandId: {}", brandId);
        return Optional.of(dto);
    }

    @Override
    public List<BrandDTO> getAllBrands() {
        log.info("Entering getAllBrands()");
        List<Brand> brands = brandRepository.findAll();
        log.info("Found {} brands in DB", brands.size());
        List<BrandDTO> brandDTOs = new ArrayList<>();
        for (Brand brand : brands) {
            log.debug("Mapping brand: {} (ID: {})", brand.getBrandName(), brand.getBrandId());
            BrandDTO dto = new BrandDTO();
            dto.setBrandId(brand.getBrandId());
            dto.setBrandName(brand.getBrandName());
            dto.setBrandDescription(brand.getBrandDescription());
            if (brand.getHotels() != null) {
                log.debug("Brand {} has {} hotels", brand.getBrandName(), brand.getHotels().size());
                Set<HotelOnlyDTO> hotelDTOs = brand.getHotels().stream()
                    .map(hotel -> {
                        HotelOnlyDTO hotelDTO = new HotelOnlyDTO();
                        hotelDTO.setHotelId(hotel.getHotelId());
                        hotelDTO.setHotelName(hotel.getHotelName());
                        return hotelDTO;
                    })
                    .collect(Collectors.toSet());
                dto.setHotels(hotelDTOs);
            } else {
                log.debug("Brand {} has no hotels", brand.getBrandName());
                dto.setHotels(Collections.emptySet());
            }
            brandDTOs.add(dto);
        }
        log.info("Returning {} BrandDTOs", brandDTOs.size());
        return brandDTOs;
    }

    @Override
    @Transactional
    public BrandDTO updateBrand(Long brandId, BrandDTO brandsDetails) {
        log.info("Entering updateBrand() with brandId: {}", brandId);
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + brandId));
        log.debug("Found brand: {}", brand);
        if (brandsDetails.getBrandDescription() != null) {
            brand.setBrandDescription(brandsDetails.getBrandDescription());
        }
        brandRepository.save(brand);
        brandsDetails.setBrandId(brand.getBrandId());
        log.info("Updated brand with ID: {}", brand.getBrandId());
        return brandsDetails;
    }

    @Override
    @Transactional
    public void deleteBrand(Long brandId) {
        log.info("Entering deleteBrand() with brandId: {}", brandId);
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + brandId));
        log.debug("Found brand: {}", brand);
        brandRepository.delete(brand);
        log.info("Deleted brand with ID: {}", brandId);
    }

    @Override
    public BrandDTO getAllHotelsByBrandId(Long brandId) {
        log.info("Entering getAllHotelsByBrandId() with brandId: {}", brandId);
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + brandId));
        log.debug("Found brand: {}", brand);
        BrandDTO dto = new BrandDTO();
        dto.setBrandId(brand.getBrandId());
        dto.setBrandName(brand.getBrandName());
        dto.setBrandDescription(brand.getBrandDescription());
        if (brand.getHotels() != null) {
            log.debug("Brand {} has {} hotels", brand.getBrandName(), brand.getHotels().size());
            Set<HotelOnlyDTO> hotelDTOs = brand.getHotels().stream()
                .map(hotel -> {
                    HotelOnlyDTO hotelDTO = new HotelOnlyDTO();
                    hotelDTO.setHotelId(hotel.getHotelId());
                    hotelDTO.setHotelName(hotel.getHotelName());
                    return hotelDTO;
                })
                .collect(Collectors.toSet());
            dto.setHotels(hotelDTOs);
        } else {
            log.debug("Brand {} has no hotels", brand.getBrandName());
            dto.setHotels(Collections.emptySet());
        }
        log.info("Returning BrandDTO for getAllHotelsByBrandId with brandId: {}", brandId);
        return dto;
    }
}