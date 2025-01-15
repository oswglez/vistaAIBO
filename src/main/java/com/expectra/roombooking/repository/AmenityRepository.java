package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.model.Room;
import jakarta.persistence.OneToMany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
    void deleteById(Long amenityId);

    // Metodo personalizado para buscar amenidades por código
    List<Amenity> findAllAmenitiesByRoomId(Integer code);


    @OneToMany(mappedBy = "hotel") // Indica el lado inverso de la relación
    public List<Media> media =  new ArrayList<>();

    // Método personalizado para encontrar todas las medias de una habitación específica
    @Query("SELECT p FROM Media p JOIN RoomMedia rp ON p.id = rp.mediaId WHERE rp.roomId = :roomId")
    List<Amenity> findAllAmenitiessByRoomId(Long roomId);

    // Método personalizado para encontrar todas las medias de un hotel específico
    @Query("SELECT p FROM Media p JOIN RoomMedia rp ON p.id = rp.mediaId WHERE rp.roomId = :roomId")
    List<Amenity> findAllAmenitiesByHotelId(Long HotelId);
}
