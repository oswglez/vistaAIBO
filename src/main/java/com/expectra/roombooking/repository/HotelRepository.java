package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    // Metodo para buscar todos los hoteles
    @Override
    List<Hotel> findAll();

    // Metodo para buscar un hotel por ID
    @Override
    Optional<Hotel> findById(Long id);

    // Metodo para guardar un hotel (crear o actualizar)
    @Override
    <S extends Hotel> S save(S entity);

    // Metodo para eliminar un hotel por entidad
    @Override
    void delete(Hotel entity);

    // Metodo para eliminar un hotel por ID
    @Override
    void deleteById(Long id);

    // Metodo personalizado para buscar hoteles por nombre
    List<Hotel> findByName(String name);

    // Metodo personalizado para buscar hoteles por categoría
    List<Hotel> findByCategory(String category);

    // Metodo personalizado para encontrar todas las amenities de un hotel específico
    List<Amenity> findAllByHotelIdAndAmenities(Long hotelId);
}

