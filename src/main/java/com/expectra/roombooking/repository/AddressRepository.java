package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    // Find all addresses associated with a contact
    @Query("SELECT a FROM Address a JOIN a.contacts c WHERE c.contactId = :contactId")
    List<Address> findAllAddressByContactId(Long contactId);

    // Método personalizado para encontrar todas las address de un hotel específico

//    @Query("SELECT a FROM Address a JOIN a.hotels h WHERE h.hotelId = :hotelId")
//    List<Address> findAllAddressByHotelId(Long hotelId);

}
