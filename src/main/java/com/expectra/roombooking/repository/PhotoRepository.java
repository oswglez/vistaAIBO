package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    // Método para buscar todas las fotos
    @Override
    List<Photo> findAll();

    // Método para buscar una foto por ID
    @Override
    Optional<Photo> findById(Long id);

    // Método para guardar una foto (crear o actualizar)
    @Override
    <S extends Photo> S save(S entity);

    // Método para eliminar una foto por entidad
    @Override
    void delete(Photo entity);

    // Método para eliminar una foto por ID
    @Override
    void deleteById(Long id);

    // Método personalizado para buscar fotos por código
    List<Photo> findByCode(String code);

    // Método personalizado para encontrar todas las fotos de una habitación específica
    @Query("SELECT p FROM Photo p JOIN RoomPhoto rp ON p.id = rp.photoId WHERE rp.roomId = :roomId")
    List<Photo> findAllByRoomId(Integer roomId);
}


