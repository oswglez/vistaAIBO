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
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping
    public List<Address> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Long id) {
        Address address = addressService.getAddressById(id);
        return ResponseEntity.ok(address);
    }

    @PostMapping
    public Address createAddress(@RequestBody Address address) {
        return addressService.createAddress(address);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long id, @RequestBody Address addressDetails) {
        Address updatedAddress = addressService.updateAddress(id, addressDetails);
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("contact/{contactId}/address/{addressId}")
    @Operation(summary = "Elimina una direccion", description = "Elimina o desconecta una direccion de un contacto.")
    public ResponseEntity<Void> removeContctFromAddress(
            @PathVariable Long contactId,
            @PathVariable Long addressId) {
        addressService.removeAddressFromContact(contactId, addressId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/hotel/{hotelId}/address/{addressId}")
    @Operation(summary = "Elimina una direccion", description = "Elimina o desconecta una direccion de un hotel.")
    public ResponseEntity<Void> removeAmenityFromHotel(
            @PathVariable Long hotelId,
            @PathVariable Long addressId) {
        addressService.removeAddressFromContact(hotelId, addressId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}