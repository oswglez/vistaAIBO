package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    // Método para buscar todos los videos
    @Override
    List<Video> findAll();

    // Método para buscar un video por ID
    @Override
    Optional<Video> findById(Long id);

    // Método para guardar un video (crear o actualizar)
    @Override
    <S extends Video> S save(S entity);

    // Método para eliminar un video por entidad
    @Override
    void delete(Video entity);

    // Método para eliminar un video por ID
    @Override
    void deleteById(Long id);

    // Método personalizado para buscar videos por habitación ID
    List<Video> findByRoomId(Integer roomId);
}

