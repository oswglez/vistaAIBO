package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.*;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    // Metodo para buscar todos los hoteles
    @Override
    @NonNull
    List<Hotel> findAll();

    // Metodo para buscar un hotel por ID
    @Override
    @NonNull
    Optional<Hotel> findById(@NonNull Long hotelId);

    // Metodo para guardar un hotel (crear o actualizar)
    @Override
    @NonNull
    <S extends Hotel> S save(@NonNull S entity);

    // Metodo para eliminar un hotel por entidad
    @Override
    void delete(@NonNull Hotel entity);

    // Metodo para eliminar un hotel por ID
    @Override
    void deleteById(@NonNull Long id);

//    @OneToMany(mappedBy = "hotel") // Indica el lado inverso de la relación
//    List<Room> room =  new ArrayList<>();

    // Metodo personalizado para buscar hoteles por nombre
    List<Hotel> findByHotelName(String hotelName);

    // Metodo personalizado para encontrar todas las amenities de un hotel específico
    @Query("SELECT a FROM Amenity a JOIN a.hotels h WHERE h.hotelId = :hotelId")
    List<Amenity> findAllAmenitiesByHotelId(@NonNull Long hotelId);

    @Query("SELECT m FROM Media m JOIN m.hotels h WHERE h.hotelId = :hotelId")
    List<Media> findAllMediasByHotelId(@Param("hotelId") @NonNull Long hotelId);

    @Query("SELECT r FROM Room r WHERE r.hotel.hotelId = :hotelId")
    List<Room> findAllRoomsByHotelId(@Param("hotelId") @NonNull Long hotelId);

    @Query("SELECT c FROM Contact c JOIN c.hotels h WHERE h.hotelId = :hotelId")
    List<Contact> findAllContactsByHotelId(@Param("hotelId") @NonNull Long hotelId);

    @Query("SELECT a FROM Address a JOIN a.hotels h WHERE h.hotelId = :hotelId")
    List<Address> findAllAddressesByHotelId(@Param("hotelId") @NonNull Long hotelId);


}

