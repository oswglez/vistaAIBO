package com.expectra.roombooking.controller;

import com.expectra.roombooking.dto.BrandDTO;
import com.expectra.roombooking.dto.HotelDTO;
import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Brand;
import com.expectra.roombooking.model.Chain;
import com.expectra.roombooking.repository.ChainRepository;
import com.expectra.roombooking.service.BrandService;
import com.expectra.roombooking.service.ChainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/brand")
@Tag(name = "Room Management", description = "Endpoints para gestión de amenities para hoteles y habitaciones")

@CrossOrigin(origins = "*")
public class BrandController {

    private final BrandService brandService;
    private final ChainService chainService;
    private final String messageNotFound = "Room not found with ID: ";

    @Autowired
    public BrandController(final BrandService brandService, final ChainService chainService) {
        this.brandService = brandService;
        this.chainService = chainService;

    }

    @PostMapping
    @Operation(summary = "Crea una brand", description = "Crea una brand usando su ID.")
    public ResponseEntity<Brand> createbrand(@RequestBody Map<String, Object> requestBody) {
        Long chainId = ((Number) requestBody.get("chainId")).longValue();

        Chain chain = chainService.getChainById(chainId)
                .orElseThrow(() -> new ResourceNotFoundException("Chain not found with id: " + requestBody.get("chainId")));

        Brand brand = new Brand();
        brand.setBrandName((String) requestBody.get("brandName"));
        brand.setBrandDescription((String) requestBody.get("brandDescription"));
        brand.setChain(chain);

        Brand createdbrand = brandService.createBrand(brand);
        return ResponseEntity.ok(createdbrand);
    }

    @GetMapping("/{brandId}")
    @Operation(summary = "Obtiene una brand", description = "Recupera una brand usando su ID.")
    public ResponseEntity<BrandDTO> getbrandByIdById(@PathVariable Long brandId) {
        return brandService.getBrandById(brandId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(messageNotFound + brandId));
    }

    @GetMapping("/{brandId}/hotels")
    @Operation(summary = "Obtiene los hoteles de una brand", description = "Recupera todos los hoteles de    una brand usando su ID.")
    public ResponseEntity<BrandDTO> getAllHotelsBrandId(@PathVariable Long brandId) {
        BrandDTO brand = brandService.getAllHotelsByBrandId(brandId);
        return ResponseEntity.ok(brand);
    }

    @GetMapping
    @Operation(summary = "Obtiene todos los brands", description = "Recupera todos los brands de la base de datos.")
    public ResponseEntity<List<BrandDTO>> getAllbrands() {
        List<BrandDTO> brands = brandService.getAllBrands();
        return ResponseEntity.ok(brands);
    }

    @PutMapping("/{brandId}")
    @Operation(summary = "Actualiza una brand", description = "Actualiza los datos de una brand existente usando su Id.")
    public ResponseEntity<BrandDTO> updatebrand(@PathVariable Long brandId, @RequestBody BrandDTO brandsDetails) {
        BrandDTO updatedbrand = brandService.updateBrand(brandId, brandsDetails);
        return ResponseEntity.ok(updatedbrand);
    }

    @DeleteMapping("/{brandId}")
    @Operation(summary = "Elimina una brand", description = "Elimina una brand existente usando su Id.")
    public ResponseEntity<Brand> deletebrand(@PathVariable Long brandId) {
        brandService.deleteBrand(brandId);
        return ResponseEntity.noContent().build();
    }
}