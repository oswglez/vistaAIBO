package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  AmenityRepository extends JpaRepository<Amenity, Long> {

    // Método para buscar todas las amenidades
    @Override
    List<Amenity> findAll();

    // Método para buscar una amenidad por ID
    @Override
    Optional<Amenity> findById(Long id);

    // Método para guardar una amenidad (crear o actualizar)
    @Override
    <S extends Amenity> S save(S entity);

    // Método para eliminar una amenidad por entidad
    @Override
    void delete(Amenity entity);

    // Método para eliminar una amenidad por ID
    @Override
    void deleteById(Long id);

    // Método personalizado para buscar amenidades por código
    List<Amenity> findByCode(String code);

    // Método personalizado para buscar amenidades por código
    List<Amenity> findAllAmenitiesByHotelId(Long hotelId);
}
