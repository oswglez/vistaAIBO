package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    // Métodos personalizados si son necesarios
}