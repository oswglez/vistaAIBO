package com.expectra.roombooking.controller;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.MediaTypes;
import com.expectra.roombooking.service.MediaTypesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    mediaTypes.setMediaTypeDescription((String) requestBody.get("mediaTypeDescription"));

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
public ResponseEntity<Page<MediaTypes>> getAllMedias(
    @PageableDefault(page = 0, size = 10) // Valores por defecto para la paginación (0-indexed page)
    @SortDefault(sort = "mediaTypeName", direction = Sort.Direction.ASC) // Ordenamiento por defecto
    Pageable pageable) {
    Page<MediaTypes> mediaTypes = mediaTypesService.getAllMediaTypes(pageable);
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