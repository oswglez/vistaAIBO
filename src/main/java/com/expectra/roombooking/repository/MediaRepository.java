package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {

    // Método para buscar todas las fotos
    @Override
    List<Media> findAll();

    // Método para buscar una foto por ID
    @Override
    Optional<Media> findById(Long mediaId);

    // Método para guardar una foto (crear o actualizar)
    @Override
    <S extends Media> S save(S entity);

    // Método para eliminar una foto por entidad
    @Override
    void delete(Media entity);

    // Método para eliminar una foto por ID
    @Override
    void deleteById(Long mediaId);

    // Método personalizado para encontrar todas las medias de una habitación específica
    @Query("SELECT m FROM Media m JOIN m.rooms r WHERE r.roomId = :roomId")

    List<Media> findAllMediasByRoomId(Long roomId);

    // Método personalizado para encontrar todas las medias de un hotel específic

    @Query("SELECT m FROM Media m JOIN m.hotels h WHERE h.hotelId = :hotelId")
    List<Media> findAllMediasByHotelId(@Param("hotelId") Long hotelId);

}
