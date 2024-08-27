package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  AmenityRepository extends JpaRepository<Amenity, Long> {

    // Metodo para buscar todas las amenidades
    @Override
    List<Amenity> findAll();

    // Metodo para buscar una amenidad por ID
    @Override
    Optional<Amenity> findById(Long id);

    // Metodo para guardar una amenidad (crear o actualizar)
    @Override
    <S extends Amenity> S save(S entity);

    // Metodo para eliminar una amenidad por entidad
    @Override
    void delete(Amenity entity);

    // Metodo para eliminar una amenidad por ID
    @Override
    void deleteById(Long id);

    // Metodo personalizado para buscar amenidades por código
    List<Amenity> findByCode(String code);

    // Metodo personalizado para buscar amenidades por código
    List<Amenity> findAllAmenitiesByHotelId(Long hotelId);
}
