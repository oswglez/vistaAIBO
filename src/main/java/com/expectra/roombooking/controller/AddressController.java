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

    private final String messageNotFound = "Address not found with ID: ";
    private final String hotelNotFound = "Hotel not found with ID: ";
    private final String contactNotFound = "Contact not found with ID: ";

    @GetMapping
    @Operation(summary = "Consulta todas las direcciones", description = "Consulta. todas las direcciones de un hotel.")
    public List<Address> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @GetMapping("/{addressId}")
    @Operation(summary = "Consulta una. direccion por Id", description = "Elimina o desconecta una direccion de un hotel.")
    public ResponseEntity<Address> getAddressById(@PathVariable Long addressId) {
        Address address = addressService.getAddressById(addressId);
        return ResponseEntity.ok(address);
    }

    @PostMapping
    @Operation(summary = "Crear una direccion", description = "Elimina o desconecta una direccion de un hotel.")
    public Address createAddress(@RequestBody Address address) {
        return addressService.createAddress(address);
    }

    @PutMapping("/{addressId}")
    @Operation(summary = "Actualza una. direccion por Id", description = "Elimina o desconecta una direccion de un hotel.")
    public ResponseEntity<Address> updateAddress(@PathVariable Long addressId, @RequestBody Address addressDetails) {
        Address updatedAddress = addressService.updateAddress(addressId, addressDetails);
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("contact/{contactId}/address/{addressId}")
    @Operation(summary = "Remueve una direccion de un contacto", description = "Elimina o desconecta una direccion de un contacto.")
    public ResponseEntity<Void> removeContactFromAddress(
            @PathVariable Long contactId,
            @PathVariable Long addressId) {
        addressService.removeAddressFromContact(contactId, addressId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/hotel/{hotelId}/address/{addressId}")
    @Operation(summary = "Remueve una direccion de un hotel", description = "Elimina o desconecta una direccion de un hotel.")
    public ResponseEntity<Void> removeAmenityFromHotel(
            @PathVariable Long hotelId,
            @PathVariable Long addressId) {
        addressService.removeAddressFromHotel(hotelId, addressId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("/{addressId}")
    @Operation(summary = "Elimina una direccion", description = "Elimina o desconecta una direccion de un hotel.")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }
}