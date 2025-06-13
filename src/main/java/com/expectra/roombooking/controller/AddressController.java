package com.expectra.roombooking.controller;

import com.expectra.roombooking.model.Address;
import com.expectra.roombooking.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins = "*")

public class AddressController {

    @Autowired
    private AddressService addressService;
    public AddressController(final AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    @Operation(summary = "Get all addresses", description = "Retrieves all addresses from the database.")
    public ResponseEntity<List<Address>> getAllAddresses() {
        List<Address> addresses = addressService.getAllAddresses();
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/{addressId}")
    @Operation(summary = "Get an address", description = "Retrieves an address using its ID.")
    public ResponseEntity<Address> getAddressById(@PathVariable Long addressId) {
        Address address = addressService.getAddressById(addressId);
        return ResponseEntity.ok(address);
    }

    @PostMapping
    @Operation(summary = "Create an address", description = "Creates a new address.")
    public ResponseEntity<Address> createAddress(@RequestBody Address address) {
        Address savedAddress = addressService.createAddress(address);
        return new ResponseEntity<>(savedAddress, HttpStatus.CREATED);
    }

    @PutMapping("/{addressId}")
    @Operation(summary = "Update an address", description = "Updates an existing address using its ID.")
    public ResponseEntity<Address> updateAddress(
            @PathVariable Long addressId,
            @RequestBody Address addressDetails) {
        Address updatedAddress = addressService.updateAddress(addressId, addressDetails);
        return ResponseEntity.ok(updatedAddress);
    }
    @DeleteMapping("/{addressId}")
    @Operation(summary = "Delete an address", description = "Deletes an existing address using its ID.")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }

    // Unlink address from a contact
    @DeleteMapping("/contacts/{contactId}/addresses/{addressId}")
    @Operation(summary = "Get addresses by contact", description = "Retrieves all addresses associated with a specific contact.")
    public ResponseEntity<Void> removeAddressFromContact(
            @PathVariable Long contactId,
            @PathVariable Long addressId) {
        addressService.removeAddressFromContact(contactId, addressId);
        return ResponseEntity.noContent().build();
    }
    // Unlink address from a hotel
    @DeleteMapping("/hotels/{hotelId}/addresses/{addressId}")
    @Operation(summary = "Get addresses by hotel", description = "Retrieves all addresses associated with a specific hotel.")
    public ResponseEntity<Void> removeAddressFromHotel(
            @PathVariable Long hotelId,
            @PathVariable Long addressId) {
        addressService.removeAddressFromHotel(hotelId, addressId);
        return ResponseEntity.noContent().build();
    }
}