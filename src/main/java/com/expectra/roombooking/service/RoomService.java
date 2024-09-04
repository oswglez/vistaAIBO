package com.expectra.roombooking.service;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.model.Room;
import com.expectra.roombooking.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    public Room updateRoom(Long id, Room roomDetails) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        room.setRoomType(roomDetails.getRoomType());
        room.setSize(roomDetails.getSize());

        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        roomRepository.delete(room);
    }
    // Método personalizado para encontrar todas las habitaciones de un hotel específico
   public  List<Room> findAllAvailableRooms(Long hotelId, Long roomId){
        List<Room> availableOracle = new ArrayList<>();
//
// buscar reservacion por hotelId &  reservarionId
// se accede a el API de oracle que nos devuelve el roomType y la fechaReservada
//


//
// buscar habitaciones disponibles de ese tipo para esa fecha
// se accede a el API de oracle con hotelId, roomType y fecha de la reservacion  y
// nos devuelve una lista de las habitaciones disponibles
//
        return availableOracle;
   }


    // Metodo personalizado para encontrar todas los medias de una habitación específica
    // public  List<Media> getMediaByHotelIdAndRoomId(Long hotelId, Long roomId) {
    public  List<Media> getAllMediasByHotelIdAndRoomId(Long hotelId, Long roomId) {

//
// buscar directamente en la base de datos de medias
//
        return roomRepository.getAllMediasByHotelIdAndRoomId(hotelId, roomId);
    }
    
    // Metodo personalizado para encontrar todas los medias de un tipo de habitación específica
    public  List<Media>getMediaByRoomType(Long hotelId, String roomType) {
//
// buscar directamente en la base de datos de medias
//
        return roomRepository.getAllMediasByHotelIdAndRoomType(hotelId, roomType);
    }


    // Método personalizado para encontrar todas los amenities de un hotel específico
    public  List<Amenity> getAmenitiesByHotelIdAndRoomId(Long hotelId, Long roomId){
//
// buscar reservacion por hotelId &  reservasionId
// se accede a el API de oracle que nos devuelve el roomType y la fechaReservada
//


//
// buscar habitaciones disponibles de ese tipo para esa fecha
// se accede a el API de oracle con hotelId, roomType y fecha de la reservacion  y
// nos devuelve una lista de las habitaciones disponibles
//
        return roomRepository.getAllAmenitiesByHotelIdAndRoomId(hotelId, roomId);
    }

    // Metodo personalizado para encontrar todas los amenities de un hotel específico
    public  List<Amenity> getAmenitiesByHotelIdAndRoomType(Long hotelId, String roomType){
//
// buscar reservacion por hotelId &  reservasionId
// se accede a el API de oracle que nos devuelve el roomType y la fechaReservada
//


//
// buscar habitaciones disponibles de ese tipo para esa fecha
// se accede a el API de oracle con hotelId, roomType y fecha de la reservacion  y
// nos devuelve una lista de las habitaciones disponibles
//
        return roomRepository.getAllAmenitiesByHotelIdAndRoomType(hotelId, roomType);
    }
}

