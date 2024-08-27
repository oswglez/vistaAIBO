package com.expectra.roombooking.controller;

import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class HotelController {
    private final HotelRepository hotelRepository;
    @Value("${spring.application.name}")
    String appName;

    public HotelController(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @GetMapping(value = "/hotel/{Id}")
    public Optional<Hotel> getHotelById(Long Id) {
        return Optional.of(hotelRepository.getReferenceById(Id));
    }

}
