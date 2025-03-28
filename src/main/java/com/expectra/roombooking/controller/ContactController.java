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
    private final String messageNotFound = "Contact not found with ID: ";

    @Autowired
    public ContactController(final ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    @Operation(summary = "Consulta todos los  contactos", description = "Consulta todos los contactos")
    public ResponseEntity<List<Contact>> getAllContacts() {
        List<Contact> contacts = contactService.findAll();
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/{contactId}")
    @Operation(summary = "Consulta un contacto por Id", description = "Consulta de un contacto a traves de su Id.")
    public ResponseEntity<Contact> getContactById(@PathVariable Long contactId) {
        return contactService.findById(contactId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(messageNotFound + contactId));
    }
    // Get Hotels by Name
    @GetMapping("/search")
    @Operation(summary = "Consulta de contactos por lastName", description = "Consulta un contacto por su nombre.")
    public ResponseEntity<List<Contact>> getContactByLastName(@RequestParam String lastName) {
        List<Contact> contacts = contactService.findContactByLastName(lastName);
        if (contacts.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron contactos con el apellido: " + lastName);
        }
        return ResponseEntity.ok(contacts);
    }
    @PostMapping
    @Operation(summary = "Crea un contacto", description = "Creación de un contacto")
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {
        Contact savedContact = contactService.save(contact);
        return new ResponseEntity<>(savedContact, HttpStatus.CREATED);
    }

    @PutMapping("/{contactId}")
    @Operation(summary = "Actualiza un contacto", description = "Actualiza los datos de un contacto a traves de su Id.")
    public ResponseEntity<Contact> updateContact
            (@PathVariable Long contactId,
             @RequestBody Contact contactDetails) {
        return contactService.findById(contactId)
                .map(existingContact -> {
                    existingContact.setFirstName(contactDetails.getFirstName());
                    existingContact.setLastName(contactDetails.getLastName());
                    existingContact.setContactTitle(contactDetails.getContactTitle());
                    existingContact.setContactEmail(contactDetails.getContactEmail());
                    existingContact.setContactMobileNumber(contactDetails.getContactMobileNumber());
                    existingContact.setContactLocalNumber(contactDetails.getContactLocalNumber());
                    existingContact.setContactFaxNumber(contactDetails.getContactFaxNumber());
                    Contact updatedContact = contactService.save(existingContact);
                    return ResponseEntity.ok(updatedContact);
                })
                .orElseThrow(() -> new ResourceNotFoundException(messageNotFound + contactId));
    }

    @DeleteMapping("/hotels/{hotelId}/contacts/{contactId}")
    @Operation(summary = "Remueve un contacto de un hotel", description = "Elimina o desconecta un contacto de un hotel.")
    public ResponseEntity<Void> removeContactFromHotel(
            @PathVariable Long hotelId,
            @PathVariable Long contactId) {
        contactService.removeContactFromHotel(hotelId, contactId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{contactId}")
    @Operation(summary = "Elimina un contacto", description = "Elimina contacto a traves de su Id.")
    public ResponseEntity<Void> deleteContact(@PathVariable Long contactId) {
        contactService.delete(contactId);
        return ResponseEntity.noContent().build();
    }
}