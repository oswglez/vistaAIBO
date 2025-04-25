package com.expectra.roombooking.controller;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.MediaTypes;
import com.expectra.roombooking.service.MediaTypesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mediaType")
@Tag(name = "Media Management", description = "Endpoints para gestión de medias para hoteles y habitaciones")

@CrossOrigin(origins = "*")
public class MediaTypeController {

    private final MediaTypesService mediaTypesService;
    private final String messageNotFound = "Media not found with ID: ";

    @Autowired
    public MediaTypeController(final MediaTypesService mediaTypesService) {
        this.mediaTypesService = mediaTypesService;
    }

@PostMapping
public ResponseEntity<MediaTypes> createMediaType(@RequestBody Map<String, Object> requestBody) {
    MediaTypes mediaTypes = new MediaTypes();
    mediaTypes.setMediaTypeName((String) requestBody.get("mediaTypeName"));

    MediaTypes createdMediaTypes = mediaTypesService.createMediaTypes(mediaTypes);
    return ResponseEntity.ok(createdMediaTypes);
}
@GetMapping("/{mediaTypeId}")
@Operation(summary = "Obtiene una mediaType", description = "Recupera una mediaType usando su ID.")
public ResponseEntity<MediaTypes> getMediaTypeByIdById(@PathVariable Long mediaTypeId) {
    return mediaTypesService.getMediaTypesById(mediaTypeId)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException(messageNotFound + mediaTypeId));
}

@GetMapping
@Operation(summary = "Obtiene todas las media", description = "Recupera todas las medias de la base de datos.")
public ResponseEntity<List<MediaTypes>> getAllMedias() {
    List<MediaTypes> mediaTypes = mediaTypesService.getAllMediaTypes();
    return ResponseEntity.ok(mediaTypes);
}

@PutMapping("/{mediaTypeId}")
@Operation(summary = "Actualiza una mediaType", description = "Actualiza los datos de una ia Type existente usando su Id.")
public ResponseEntity<MediaTypes> updateMediaType(@PathVariable Long mediaTypeId, @RequestBody MediaTypes mediaTypeDetails) {
    MediaTypes updatedMediaTypes = mediaTypesService.updateMediaTypes(mediaTypeId, mediaTypeDetails);
    return ResponseEntity.ok(updatedMediaTypes);
}

@DeleteMapping("/{mediaTypeId}")
@Operation(summary = "" +
        "Elimina una mediaType", description = "Elimina una media Type existente usando su Id.")
public ResponseEntity<MediaTypes> deleteMediaType(@PathVariable Long mediaTypeId) {
    mediaTypesService.deleteMediaTypes(mediaTypeId);
    return ResponseEntity.noContent().build();
}

}