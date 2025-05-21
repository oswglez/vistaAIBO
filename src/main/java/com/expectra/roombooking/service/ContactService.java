package com.expectra.roombooking.service;

import com.expectra.roombooking.model.Contact;
import java.util.List;
import java.util.Optional;

public interface ContactService {
    List<Contact> findAll();
    Optional<Contact> findById(Long id);
    Contact save(Contact contact);
    Contact update(Long id, Contact contact);
    void delete(Long id);
    void removeContactFromHotel(Long contactId, Long hotelId);
    List<Contact> findContactByLastName(String lastName);
    Optional<Contact> findByContactEmailIgnoreCase(String contactEmail);


}