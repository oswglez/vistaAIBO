package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Room;
import com.expectra.roombooking.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    // Método para buscar todas las habitaciones
    @Override
    List<Room> findAll();

    // Método para buscar una habitación por ID
    @Override
    Optional<Room> findById(Long id);

    // Método para guardar una habitación (crear o actualizar)
    @Override
    <S extends Room> S save(S entity);

    // Método para eliminar una habitación por entidad
    @Override
    void delete(Room entity);

    // Método para eliminar una habitación por ID
    @Override
    void deleteById(Long id);

    // Método personalizado para buscar habitaciones por hotel ID
    List<Room> findByHotelId(Long hotelId);

    // Método personalizado para buscar habitaciones por tamaño
    List<Room> findBySize(Integer size);

    // Método personalizado para encontrar todas las habitaciones de un hotel específico
    List<Room> findAllByHotelIdAndRoomType(Long hotelId, Integer roomType);


    // Método personalizado para encontrar todas las mmedias de una habitacion específica
    List<Video> getAllMediasByHotelIdAndRoomCode(Long hotelId, Integer roomCode);
}
