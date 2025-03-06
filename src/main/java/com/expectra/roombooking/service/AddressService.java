package com.expectra.roombooking.service;

import com.expectra.roombooking.model.*;

import java.util.List;


public interface AddressService {
    // Operaciones CRUD básicas
    List<Address> getAllAddresses();
    Address getAddressById(Long id);
    Address createAddress(Address address);
    Address updateAddress(Long id, Address addressDetails);
    void deleteAddress(Long id);
    void removeAddressFromContact(Long contactId, Long addressId);
    void removeAddressFromHotel(Long hotelId, Long addressId);
}