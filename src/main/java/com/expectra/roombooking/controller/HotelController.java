package com.expectra.roombooking.controller;

import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.model.Room;
import com.expectra.roombooking.repository.HotelRepository;
import com.expectra.roombooking.service.HotelService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "hotel")
public class HotelController {

    private final HotelService hotelService;
    @Value("${spring.application.name}")
    String appName;

    public HotelController(HotelService hotelService) {
        this.hotelService= hotelService;
    }

    @GetMapping(value = "/{Id}")
    public ResponseEntity<Optional<Hotel>> getHotelById(@PathVariable Long Id) {
        System.out.println("entro en el controlador");
        Optional<Hotel> hotel = hotelService.getHotelById(Id);
        return ResponseEntity.ok(hotel);
    }
    @GetMapping("{hotelId}/rooms")
    public ResponseEntity<List<Room>> getRoomsByHotelId(
            @PathVariable Long hotelId) {
        List<Room> rooms = hotelService.getAllRoomsByHotelId(hotelId);
        if (rooms.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rooms);
    }
    @GetMapping("{hotelId}/amenities")
    public ResponseEntity<List<Amenity>> getHotelAmenities(
            @PathVariable Long hotelId) {
        List<Amenity> amenities = hotelService.getAmenitiesByHotelId(hotelId);
        if (amenities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(amenities);
    }
    @GetMapping("{hotelId}/medias")
    public ResponseEntity<List<Media>> getHotelMedias(
            @PathVariable Long hotelId) {
        List<Media> medias = hotelService.getMediasByHotelId(hotelId);
        if (medias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(medias);
    }
}
