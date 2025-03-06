package com.expectra.roombooking.controller;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Contact;
import com.expectra.roombooking.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@Tag(name = "Contact Management", description = "Endpoints para gestión de contactos")

public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Contact>> getAllContacts() {
        return ResponseEntity.ok(contactService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulta un contacto", description = "Consulta de un contacto a traves de su Id.")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        return contactService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    // Get Hotels by Name
    @GetMapping("/{lastName}")
    @Operation(summary = "Consulta de contactos", description = "Consulta un contacto por su nombre.")
    public ResponseEntity<List<Contact>> getContactByLastName(@RequestParam String name) {
        List<Contact> contacts = contactService.findContactByLastName(name);
        if (contacts.isEmpty()) {
            System.out.println("name = " + name);
            throw new ResourceNotFoundException("No se encontraron contactos con el nombre: " + name);
        }
        return ResponseEntity.ok(contacts);
    }
    @PostMapping
    @Operation(summary = "Crea un contacto", description = "Creación de un contacto")
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {
        Contact savedContact = contactService.save(contact);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedContact);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un contacto", description = "Actualiza los datos de un contacto a traves de su Id.")
    public ResponseEntity<Contact> updateContact(@PathVariable Long id, @RequestBody Contact contact) {
        try {
            Contact updatedContact = contactService.update(id, contact);
            return ResponseEntity.ok(updatedContact);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/hotel/{hotelId}/contact/{contactId}")
    @Operation(summary = "Elimina un.   contacto", description = "Elimina o desconecta un contacto de un hotel.")
    public ResponseEntity<Void> removeContactFromHotel(
            @PathVariable Long hotelId,
            @PathVariable Long contactId) {
        contactService.removeContactFromHotel(hotelId, contactId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un contacto", description = "Elimina o desconecta de un hotel un contacto a traves de su Id.")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.delete(id);
        return ResponseEntity.noContent().build();
    }
}