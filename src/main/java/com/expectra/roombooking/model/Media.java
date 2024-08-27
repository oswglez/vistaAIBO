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

public class Media {

    @jakarta.persistence.Id
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String mediaType;

    private String url;



    @OneToMany(mappedBy = "media")

    private List<RoomMedia> roomMedia;



    @OneToMany(mappedBy = "media")

    private List<HotelMedia> hotelMedia;

    // Getters and setters

}
