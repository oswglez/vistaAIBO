package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Amenity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
    public interface  AmenityRepository extends JpaRepository<Amenity, Long> {
//
//    // Metodo para buscar todas las amenidades
//    @Override
//    List<Amenity> findAll();
//
//    // Metodo para buscar una amenidad por ID
//    @Override
//    Optional<Amenity> findById(Long id);
//
//    // Metodo para guardar una amenidad (crear o actualizar)
//    @Override
//    <S extends Amenity> S save(S entity);
//
//    // Metodo para eliminar una amenidad por entidad
//    @Override
//    void delete(Amenity entity);
//
//    // Metodo para eliminar una amenidad por ID
//    @Override
//    void deleteById(Long amenityId);


    // Método personalizado para encontrar todas las medias de una habitación específica
    @Query("SELECT a FROM Amenity a JOIN a.rooms r WHERE r.roomId = :roomId")
    List<Amenity> findAllAmenitiesByRoomId(Long roomId);

    // Método personalizado para encontrar todas las medias de un hotel específico
    @Query("SELECT a FROM Amenity a JOIN a.hotels h WHERE h.hotelId = :hotelId")
    List<Amenity> findAllAmenitiesByHotelId(Long hotelId);

}
