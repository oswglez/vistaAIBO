package com.expectra.roombooking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;
@Data
@Entity
    public class Hotel {

    @jakarta.persistence.Id
    @Id

        @GeneratedValue(strategy = GenerationType.IDENTITY)

        private Long id;
        private String name;
        private Integer roomCount;
        private String fullAddress;

        @OneToMany(mappedBy = "hotel")

        private List<Room> rooms;

        @OneToMany(mappedBy = "hotel")

        private List<HotelMedia> hotelMedia;

        @OneToMany(mappedBy = "hotel")

        private List<HotelAmenity> hotelAmenities;


    // Getters and setters

    }
