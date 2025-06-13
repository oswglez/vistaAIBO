package com.expectra.roombooking.service.impl;

import com.expectra.roombooking.model.Contact;
import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.repository.ContactRepository;
import com.expectra.roombooking.repository.HotelRepository;
import com.expectra.roombooking.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final HotelRepository hotelRepository;


    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository, HotelRepository hotelRepository) {
        this.contactRepository = contactRepository;
        this.hotelRepository = hotelRepository;
    }

    @Override
    public List<Contact> findAll() {
        return contactRepository.findAll();
    }


    @Override
    public Optional<Contact> findById(Long id) {
        return contactRepository.findById(id);
    }

    @Override
    public List<Contact> findContactByLastName(String lastName) {
        return contactRepository.findContactByLastName(lastName);
    }

    @Override
    public Optional<Contact> findByContactEmailIgnoreCase(String contactEmail) {
        return contactRepository.findByContactEmailIgnoreCase(contactEmail);
    }

    @Override
    public Contact save(Contact contact) {
        return contactRepository.save(contact);
    }

    @Override
    public Contact update(Long id, Contact contact) {
        if (contactRepository.existsById(id)) {
            contact.setContactId(id);
            return contactRepository.save(contact);
        }
        throw new RuntimeException("Contact not found with id: " + id);
    }

    @Override
    public void delete(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));

        // Only delete if not associated with any hotel
        if (contact.getHotels().isEmpty()) {
            contactRepository.deleteById(id);
        } else {
            throw new RuntimeException("Cannot delete contact as it is still associated with hotels");
        }
    }

    @Override
    @Transactional
    public void removeContactFromHotel(Long hotelId, Long contactId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + hotelId));

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found with id: " + contactId));

        hotel.getContacts().remove(contact);
        hotelRepository.save(hotel);
        hotelRepository.flush();
        // Persistence is handled automatically due to @Transactional
        // 🔹 Check if the Contact is orphaned and delete it
        if (contact.getHotels().isEmpty() && contact.getAddresses().isEmpty()) {
            contactRepository.delete(contact);
        }
    }
}