package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    // Metodo para buscar todas las habitaciones
    @Override
    List<Room> findAll();

    // Metodo para buscar una habitación por ID
    @Override
    Optional<Room> findById(Long id);

    // Metodo para guardar una habitación (crear o actualizar)
    @Override
    <S extends Room> S save(S entity);

    // Metodo para eliminar una habitación por entidad
    @Override
    void delete(Room entity);

    // Metodo para eliminar una habitación por ID
    @Override
    void deleteById(Long id);

    // Metodo personalizado para buscar todas las habitaciones por hotel ID
    List<Room> findByHotelId(Long hotelId);

    // Metodo personalizado para buscar habitaciones por tamaño
    List<Room> findBySize(Integer size);

    // Metodo personalizado para encontrar todas las habitaciones de un tipo especifico de un hotel
    List<Room> findAllRoomsByHotelIdAndRoomType(Long hotelId, String roomType);


    // Metodo personalizado para encontrar todas las medias de una habitacion específica
    List<Media> getAllMediasByHotelIdAndRoomId(Long hotelId, Long roomId);

    // Metodo personalizado para encontrar todas las medias de una habitacion específica
    List<Media> getAllMediasByHotelIdAndRoomType(Long hotelId, String roomType);

    // Metodo personalizado para encontrar todas las amenities de una habitacion específica
    List<Amenity> getAllAmenitiesByHotelIdAndRoomId(Long hotelId, Long roomId);

    // Metodo personalizado para encontrar todas las amenities de un tipo de habitacion específica
    List<Amenity> getAllAmenitiesByHotelIdAndRoomType(Long hotelId, String roomType);
}
