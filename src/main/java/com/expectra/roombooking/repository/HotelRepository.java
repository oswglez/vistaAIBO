package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    // Método para buscar todos los hoteles
    @Override
    List<Hotel> findAll();

    // Método para buscar un hotel por ID
    @Override
    Optional<Hotel> findById(Long id);

    // Método para guardar un hotel (crear o actualizar)
    @Override
    <S extends Hotel> S save(S entity);

    // Método para eliminar un hotel por entidad
    @Override
    void delete(Hotel entity);

    // Método para eliminar un hotel por ID
    @Override
    void deleteById(Long id);

    // Método personalizado para buscar hoteles por nombre
    List<Hotel> findByName(String name);

    // Método personalizado para buscar hoteles por categoría
    List<Hotel> findByCategory(String category);

    // Método personalizado para encontrar todas las habitaciones de un hotel específico
    List<Amenity> findAllByHotelIdAndAmenities(Long hotelId);
}

