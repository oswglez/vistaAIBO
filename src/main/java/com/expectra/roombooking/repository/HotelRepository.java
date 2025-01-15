package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.model.Room;
import jakarta.persistence.OneToMany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    // Metodo para buscar todos los hoteles
    @Override
    List<Hotel> findAll();

    // Metodo para buscar un hotel por ID
    @Override
    Optional<Hotel> findById(Long hotelId);

    // Metodo para guardar un hotel (crear o actualizar)
    @Override
    <S extends Hotel> S save(S entity);

    // Metodo para eliminar un hotel por entidad
    @Override
    void delete(Hotel entity);

    // Metodo para eliminar un hotel por ID
    @Override
    void deleteById(Long id);

    @OneToMany(mappedBy = "hotel") // Indica el lado inverso de la relación
    public List<Room> room =  new ArrayList<>();

    // Metodo personalizado para buscar hoteles por nombre
    List<Hotel> findByHotelName(String hotelName);

    // Metodo personalizado para encontrar todas las amenities de un hotel específico
    @Query("SELECT a FROM Amenity a JOIN a.hotels h WHERE h.hotelId = :hotelId")
    List<Amenity> findAllAmenitiesByHotelId(Long hotelId);

    @Query("SELECT m FROM Media m JOIN m.hotels h WHERE h.hotelId = :hotelId")
    List<Media> findAllMediasByHotelId(@Param("hotelId") Long hotelId);
}

