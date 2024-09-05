package com.expectra.roombooking.service;

import com.expectra.roombooking.dto.ReservationResponseDTO;
import com.expectra.roombooking.dto.RoomsByHotelIdResponseDTO;
import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.model.Room;
import com.expectra.roombooking.repository.HotelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Optional;

//import static org.springframework.data.jpa.domain.AbstractPersistable_.id;
@Slf4j
@Service
public class HotelService {

    private final HotelRepository hotelRepository;
    private final WebClient webClient;


    @Autowired
    public HotelService(HotelRepository hotelRepository, WebClient.Builder webClientBuilder) {
        this.hotelRepository = hotelRepository;
        this.webClient = webClientBuilder.baseUrl("https://pms.example.com").build();
    }

    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public Optional<Hotel> getHotelById(Long id) {
        return hotelRepository.findById(id);
    }

    public Hotel updateHotel(Long id, Hotel hotelDetails) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        hotel.setName(hotelDetails.getName());
        hotel.setRoomCount(hotelDetails.getRoomCount());
        hotel.setFullAddress(hotelDetails.getFullAddress());

        return hotelRepository.save(hotel);
    }

    public void deleteHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));
        hotelRepository.delete(hotel);
    }

    public RoomsByHotelIdResponseDTO getAvailableRoomsByHotelId(Long hotelId, Long reservationId){
// Paso1 con el hotel y la reservacion busco los datos de la habitacion en el PMS
        RoomsByHotelIdResponseDTO responseDTO = new RoomsByHotelIdResponseDTO();
        log.warn("### Estoy en el get");
    try{
        ReservationResponseDTO response = this.webClient.get()
                .uri("http://localhost:8090/hotel/{hotelId}/{id}", hotelId, reservationId) // URLs específicas por cada solicitud
                .retrieve()
                .bodyToMono(ReservationResponseDTO.class)
                .block(); // Bloquea la ejecución hasta que obtenga la respuesta

        // Aquí puedes procesar la respuesta y hacer otra llamada o cualquier otra lógica
        System.out.println("Response from PMS: " + response);

    } catch (WebClientResponseException e) {
        // Manejo de errores específico
        System.out.println("Error in PMS request: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
    }
        log.warn("### Salio del try");

        List<Room> rooms = this.webClient.get()
                .uri("http://localhost:8090/hotel/{hotelId}/rooms", hotelId)
                .retrieve()
                .bodyToFlux(Room.class)
                .collectList()
                .block();
        log.warn("### Salio del segundo");

        responseDTO.setRoomsAvailable(rooms);
        return responseDTO;
    }
    public  List<Room> getAllRoomsByHotelId(Long hotelId){
        return hotelRepository.findAllRoomsByHotelId(hotelId);
    }

    public  List<Amenity> getAmenitiesByHotelId(Long hotelId){
        return hotelRepository.findAllAmenitiesByHotelId(hotelId);
    }

    public  List<Media> getMediasByHotelId(Long hotelId){
        return hotelRepository.findAllMediasByHotelId(hotelId);
    }

}
