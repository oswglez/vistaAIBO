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
@Tag(name = "Media Management", description = "Endpoints for managing hotel and room media")

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
@Operation(summary = "Get a media type", description = "Retrieves a media type using its ID.")
public ResponseEntity<MediaTypes> getMediaTypeByIdById(@PathVariable Long mediaTypeId) {
    return mediaTypesService.getMediaTypesById(mediaTypeId)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException(messageNotFound + mediaTypeId));
}

@GetMapping
@Operation(summary = "Get all media types", description = "Retrieves all media types from the database.")
public ResponseEntity<Page<MediaTypes>> getAllMedias(
    @PageableDefault(page = 0, size = 10) // Default pagination values (0-indexed page)
    @SortDefault(sort = "mediaTypeName", direction = Sort.Direction.ASC) // Default sorting
    Pageable pageable) {
    Page<MediaTypes> mediaTypes = mediaTypesService.getAllMediaTypes(pageable);
    return ResponseEntity.ok(mediaTypes);
}


@PutMapping("/{mediaTypeId}")
@Operation(summary = "Update a media type", description = "Updates the data of an existing media type using its ID.")
public ResponseEntity<MediaTypes> updateMediaType(@PathVariable Long mediaTypeId, @RequestBody MediaTypes mediaTypeDetails) {
    MediaTypes updatedMediaTypes = mediaTypesService.updateMediaTypes(mediaTypeId, mediaTypeDetails);
    return ResponseEntity.ok(updatedMediaTypes);
}

@DeleteMapping("/{mediaTypeId}")
@Operation(summary = "Delete a media type", description = "Deletes an existing media type using its ID.")
public ResponseEntity<MediaTypes> deleteMediaType(@PathVariable Long mediaTypeId) {
    mediaTypesService.deleteMediaTypes(mediaTypeId);
    return ResponseEntity.noContent().build();
}

}