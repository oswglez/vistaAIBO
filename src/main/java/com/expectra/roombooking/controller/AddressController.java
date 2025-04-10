package com.expectra.roombooking.controller;

import com.expectra.roombooking.model.Address;
import com.expectra.roombooking.service.AddressService;
import com.expectra.roombooking.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins = "http://localhost:5173")

public class AddressController {

    @Autowired
    private AddressService addressService;
    public AddressController(final AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    @Operation(summary = "Consulta todas las direcciones", description = "Consulta. todas las direcciones de un hotel.")
    public ResponseEntity<List<Address>> getAllAddresses() {
        List<Address> addresses = addressService.getAllAddresses();
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/{addressId}")
    @Operation(summary = "Consulta una. direccion por Id", description = "Elimina o desconecta una direccion de un hotel.")
    public ResponseEntity<Address> getAddressById(@PathVariable Long addressId) {
        Address address = addressService.getAddressById(addressId);
        return ResponseEntity.ok(address);
    }

    @PostMapping
    @Operation(summary = "Crear una nueva direccion", description = "Elimina o desconecta una direccion de un hotel.")
    public ResponseEntity<Address> createAddress(@RequestBody Address address) {
        Address savedAddress = addressService.createAddress(address);
        return new ResponseEntity<>(savedAddress, HttpStatus.CREATED);
    }

    @PutMapping("/{addressId}")
    @Operation(summary = "Actualzar una. direccion por Id", description = "Elimina o desconecta una direccion de un hotel.")
    public ResponseEntity<Address> updateAddress(
            @PathVariable Long addressId,
            @RequestBody Address addressDetails) {
        Address updatedAddress = addressService.updateAddress(addressId, addressDetails);
        return ResponseEntity.ok(updatedAddress);
    }
    @DeleteMapping("/{addressId}")
    @Operation(summary = "Elimina una direccion", description = "Elimina o desconecta una direccion de un hotel.")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }

    // Desvincular dirección de un contacto
    @DeleteMapping("/contacts/{contactId}/addresses/{addressId}")
    @Operation(summary = "Desvincular dirección de contacto", description = "Elimina la relación entre un contacto y una dirección.")
    public ResponseEntity<Void> removeAddressFromContact(
            @PathVariable Long contactId,
            @PathVariable Long addressId) {
        addressService.removeAddressFromContact(contactId, addressId);
        return ResponseEntity.noContent().build();
    }
    // Desvincular dirección de un hotel
    @DeleteMapping("/hotels/{hotelId}/addresses/{addressId}")
    @Operation(summary = "Desvincular dirección de hotel", description = "Elimina la relación entre un hotel y una dirección.")
    public ResponseEntity<Void> removeAddressFromHotel(
            @PathVariable Long hotelId,
            @PathVariable Long addressId) {
        addressService.removeAddressFromHotel(hotelId, addressId);
        return ResponseEntity.noContent().build();
    }
}