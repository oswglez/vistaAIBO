package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Contact;
import com.expectra.roombooking.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    // Métodos personalizados si son necesarios
    // Metodo personalizado para buscar hoteles por nombre
    List<Contact> findContactByLastName(String lastName);
}