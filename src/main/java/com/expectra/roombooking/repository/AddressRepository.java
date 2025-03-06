package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
    public interface AddressRepository extends JpaRepository<Address, Long> {

    // Metodo para buscar todas las amenidades
    @Override
    List<Address> findAll();

    // Metodo para buscar una amenidad por ID
    @Override
    Optional<Address> findById(Long id);

    // Metodo para guardar una amenidad (crear o actualizar)
    @Override
    <S extends Address> S save(S entity);

    // Metodo para eliminar una amenidad por entidad
    @Override
    void delete(Address entity);

    // Metodo para eliminar una amenidad por ID
    @Override
    void deleteById(Long addressId);


    @Query("SELECT a FROM Address a JOIN a.contacts c WHERE c.contactId = :contactId")
    List<Address> findAllAddressByContactId(Long contactId);

    // Método personalizado para encontrar todas las medias de un hotel específico

//    @Query("SELECT a FROM Address a JOIN a.hotels h WHERE h.hotelId = :hotelId")
//    List<Address> findAllAddressByHotelId(Long hotelId);

}
