package com.expectra.roombooking.controller;

import com.expectra.roombooking.dto.HotelDTO;
import com.expectra.roombooking.dto.HotelListDTO;
import com.expectra.roombooking.dto.HotelOnlyDTO; // Import HotelOnlyDTO
import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.*;
import com.expectra.roombooking.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hotels")
@Tag(name = "Hotel Management", description = "Endpoints para gestión de hoteles")
@CrossOrigin(origins = "*")
public class HotelController {

    private static final Logger log = LoggerFactory.getLogger(HotelController.class);
    private final String messageNotfound = "Hotel not found by Id";
    private final HotelService hotelService;
    private final ModelMapper modelMapper; // Autowire ModelMapper

    @Autowired
    public HotelController(final HotelService hotelService, final ModelMapper modelMapper) {
        this.hotelService = hotelService;
        this.modelMapper = modelMapper; // Inject ModelMapper
    }

    // Create a new Hotel
    @PostMapping("/")
    @Operation(summary = "Crea un hotel", description = "Crea un hotel usando el hotelId.")
    public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel) {
        Hotel savedHotel = hotelService.saveHotel(hotel);
        return new ResponseEntity<>(savedHotel, HttpStatus.CREATED);
    }

    // Get all Hotels
    @GetMapping
    @Operation(summary = "Consulta todos los hoteles", description = "Consulta de todos los hoteles.")
    public ResponseEntity<List<HotelOnlyDTO>> getAllHotels() { // Return HotelOnlyDTO
        List<Hotel> hotels = hotelService.findAllHotels();
        List<HotelOnlyDTO> hotelDTOs = hotels.stream()
                .map(hotel -> modelMapper.map(hotel, HotelOnlyDTO.class)) // Use ModelMapper
                .collect(Collectors.toList());
        return ResponseEntity.ok(hotelDTOs);
    }

    // Get all Hotels
    @GetMapping("/hotelList")
    @Operation(summary = "HotelList creation", description = "Query to create the hotel list")
    public ResponseEntity<List<HotelListDTO>> getAllHotelsAndRelationships() { // Return HotelOnlyDTO
        List<HotelListDTO> hotels = hotelService.findConsolidatedHotelData();
        return ResponseEntity.ok(hotels);
    }

    // Get Hotel by ID
    @GetMapping("/{hotelId}")
    @Operation(summary = "Consulta un hotel", description = "Consulta un hotel usando el hotelId.")
    public ResponseEntity<HotelOnlyDTO> getHotelById(@PathVariable Long hotelId) { // Return HotelOnlyDTO
        return hotelService.findHotelById(hotelId)
                .map(hotel -> ResponseEntity.ok(modelMapper.map(hotel, HotelOnlyDTO.class))) // Use ModelMapper
                .orElseThrow(() -> new ResourceNotFoundException(messageNotfound + hotelId));
    }

    // Get Hotel by ID
    @GetMapping(value = "/{hotelId}/roomsDTO", produces = "application/json")
    @Operation(summary = "Consulta un hotel y todas sus habitaciones por hotelId", description = "Consulta un hotel y rooms usando el hotelId.")
    public ResponseEntity<HotelDTO> getAllRoomsByHotelId(@PathVariable Long hotelId) {
        HotelDTO hotelDTO = hotelService.getHotelAndRoomsByHotelId(hotelId);
        return ResponseEntity.ok(hotelDTO);
    }

    // Get Hotel by ID
    @GetMapping(value = "/{hotelId}/roomType/{roomTypes}/roomsDTO", produces = "application/json")
    @Operation(summary = "Consulta un hotel y todas sus habitaciones por roomType", description = "Consulta un hotel y rooms usando el hotelId y roomType.")
    public ResponseEntity<HotelDTO> getHotelAndRoomsByHotelIdAndRoomType(@PathVariable Long hotelId, @PathVariable String roomTypes) {
        HotelDTO hotelDTO = hotelService.findHotelAndRoomsByHotelIdAndRoomType(hotelId, roomTypes);
        return ResponseEntity.ok(hotelDTO);
    }

    // Update Hotel
    @PutMapping("/{hotelId}")
    @Operation(summary = "Actualiza un hotel", description = "Actualiza un hotel usando el hotelId.")
    public ResponseEntity<HotelOnlyDTO> updateHotel(@PathVariable Long hotelId, @RequestBody Hotel hotelDetails) {
        return hotelService.findHotelById(hotelId)
                .map(existingHotel -> {
                    // Mantener el ID original
                    hotelDetails.setHotelId(hotelId);
                    Hotel updatedHotel = hotelService.saveHotel(hotelDetails);
                    HotelOnlyDTO hotelOnlyDTO = modelMapper.map(updatedHotel, HotelOnlyDTO.class); // Map to DTO
                    return ResponseEntity.ok(hotelOnlyDTO);
                })
                .orElseThrow(() -> new ResourceNotFoundException(messageNotfound + hotelId));
    }

//    // Delete Hotel
//    @DeleteMapping("/{hotelId}")
//    @Operation(summary = "Elimina un hotel", description = "Elimina y sus habitaciones un hotel usando el hotelId.")
//    public ResponseEntity<Void> deleteHotel(@PathVariable Long hotelId) {
//        if (hotelService.findHotelById(hotelId).isEmpty()) {
//            throw new ResourceNotFoundException(messageNotfound + hotelId);
//        }
//        log.info("Deleting hotel with ID: {}", hotelId);
//        hotelService.deleteHotelById(hotelId);
//        return ResponseEntity.noContent().build();
//    }
    // Delete logically Hotel
@DeleteMapping("/{hotelId}")
@Operation(summary = "Elimina lógicamente un hotel", description = "Marca un hotel como eliminado usando el hotelId.")
public ResponseEntity<Void> deleteHotel(@PathVariable Long hotelId) {
    Optional<Hotel> hotelOptional = hotelService.findHotelById(hotelId);

    if (hotelOptional.isPresent()) {
        // Perform the logical deletion
        hotelService.logicalDeleteHotel(hotelId);
        // Return a 204 No Content response
        return ResponseEntity.noContent().build();
    } else {
        // Throw a ResourceNotFoundException if the hotel is not found
        throw new ResourceNotFoundException("Hotel no encontrado: " + hotelId);
    }
}

 //       Optional<Hotel> hotel = hotelService.findHotelById(hotelId);
//        if (hotelService.findHotelById(hotelId).isEmpty()) {
//            throw new ResourceNotFoundException(messageNotfound + hotelId);
//        }
//        if (!hotel.ifPresent()) {
//            throw new ResourceNotFoundException(messageNotfound + hotelId);
//        };
//
//        log.info("Deleting hotel with ID: {}", hotelId);
//        hotel.setDeleted(true);
//        hotelService.deleteHotelById(hotelId);
//        return ResponseEntity.noContent().build();
//    }
    // Get Hotels by Name
    @GetMapping("/search")
    @Operation(summary = "Consulta por nombre de hotel", description = "Consulta un hotel por su nombre.")
    public ResponseEntity<List<HotelOnlyDTO>> getHotelsByName(@RequestParam String hotelName) {
        List<Hotel> hotels = hotelService.findHotelsByName(hotelName);
//        if (hotels.isEmpty()) {
//            System.out.println("name = " + hotelName);
//            throw new ResourceNotFoundException("No se encontraron hoteles con el nombre: " + hotelName);
//        }
//        return ResponseEntity.ok(hotels);

        List<HotelOnlyDTO> hotelDTOs = hotels.stream()
                .map(hotel -> modelMapper.map(hotel, HotelOnlyDTO.class)) // Use ModelMapper
                .collect(Collectors.toList());
        return ResponseEntity.ok(hotelDTOs);

    }

    // Get Hotel rooms
    @GetMapping("/{hotelid}/rooms")
    @Operation(summary = "Consulta todas las habitaciones", description = "Consulta las habbitaciones de un hotel usando el hotelId.")
    public ResponseEntity<List<Room>> findHotelRooms(@PathVariable Long hotelid) {
        if (hotelService.findHotelById(hotelid).isEmpty()) {
            throw new ResourceNotFoundException(messageNotfound + hotelid);
        }
        List<Room> rooms = hotelService.findHotelRooms(hotelid);
        return ResponseEntity.ok(rooms);
    }

    // Get Hotel Amenities
    @GetMapping("/{hotelId}/amenities")
    @Operation(summary = "Consulta todas las amenities por hotelId", description = "Consulta las amenities de un hotel usando el hotelId.")
    public ResponseEntity<List<Amenity>> getHotelAmenities(@PathVariable Long hotelId) {
        if (hotelService.findHotelById(hotelId).isEmpty()) {
            throw new ResourceNotFoundException(messageNotfound + hotelId);
        }
        List<Amenity> amenities = hotelService.findHotelAmenities(hotelId);
        return amenities.isEmpty()
                ? ResponseEntity.noContent().build() // Retorna 204 si no hay amenities
                : ResponseEntity.ok(amenities);      // Retorna 200 con la lista de amenities
    }

    // Get Hotel Media
    @GetMapping("/{hotelId}/media")
    @Operation(summary = "Consulta todas las medias", description = "Consulta las medias de un de un hotel usando el hotelId.")
    public ResponseEntity<List<Media>> getHotelMedia(@PathVariable Long hotelId) {
        if (hotelService.findHotelById(hotelId).isEmpty()) {
            throw new ResourceNotFoundException(messageNotfound + hotelId);
        }
        List<Media> medias = hotelService.findHotelMedias(hotelId);
        return medias.isEmpty()
                ? ResponseEntity.noContent().build() // Retorna 204 si no hay amenities
                : ResponseEntity.ok(medias);      // Retorna 200 con la lista de amenities
    }

    @GetMapping("/{hotelId}/contacts")
    @Operation(summary = "Consulta los contactos", description = "Consulta los contactos de un de un hotel usando el hotelId.")
    public ResponseEntity<List<Contact>> getAllContactsByHotelId(@PathVariable Long hotelId) {
        if (hotelService.findHotelById(hotelId).isEmpty()) {
            throw new ResourceNotFoundException(messageNotfound + hotelId);
        }
        List<Contact> contact = hotelService.findAllContactsByHotelId(hotelId);
        return ResponseEntity.ok(contact);
    }

    @GetMapping("/{hotelId}/addresses")
    @Operation(summary = "Consulta las direcciones", description = "Consulta las direcciones de un de un hotel usando el hotelId.")
    public ResponseEntity<List<Address>> getAllAddressesByHotelId(@PathVariable Long hotelId) {
        if (hotelService.findHotelById(hotelId).isEmpty()) {
            throw new ResourceNotFoundException(messageNotfound + hotelId);
        }
        List<Address> addresses = hotelService.findAllAddressesByHotelId(hotelId);
        return ResponseEntity.ok(addresses);
    }
}