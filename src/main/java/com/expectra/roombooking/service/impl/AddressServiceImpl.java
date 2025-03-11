// MediaServiceImpl.java
package com.expectra.roombooking.service.impl;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.*;
import com.expectra.roombooking.repository.*;
import com.expectra.roombooking.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final ContactRepository contactRepository;
    private final HotelRepository hotelRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(ContactRepository contactRepository, HotelRepository hotelRepository, AddressRepository addressRepository) {
        this.contactRepository = contactRepository;
        this.hotelRepository = hotelRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    @Override
    public Address getAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
    }

    @Override
    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(Long id, Address addressDetails) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

        address.setCountry(addressDetails.getCountry());
        address.setState(addressDetails.getState());
        address.setCity(addressDetails.getCity());
        address.setStreet(addressDetails.getStreet());
        address.setPostalCode(addressDetails.getPostalCode());

        return addressRepository.save(address);
    }
    @Override
    @Transactional
    public void removeAddressFromContact(Long contactId, Long addressId) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + contactId));
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        address.getContacts().remove(contact);
        addressRepository.save(address);
        addressRepository.flush();

        if (address.getHotels().isEmpty() && address.getContacts().isEmpty()) {
            addressRepository.delete(address); // Elimina la dirección si está huérfana
        }
    }

    @Override
    @Transactional
    public void removeAddressFromHotel(Long hotelId, Long addressId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        hotel.getAddresses().remove(address);
        hotelRepository.save(hotel);
        hotelRepository.flush();

        if (address.getHotels().isEmpty() && address.getContacts().isEmpty()) {
            addressRepository.delete(address); // Elimina la dirección si está huérfana
        }
    }
    @Override
    public void deleteAddress(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

        if (!address.getHotels().isEmpty() || !address.getContacts().isEmpty()) {
            throw new ResourceNotFoundException("address has actives relationships  id: " + id);
        }
        addressRepository.delete(address); // Elimina la dirección si está huérfana
    }
}