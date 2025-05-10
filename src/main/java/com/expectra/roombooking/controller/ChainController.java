package com.expectra.roombooking.controller;

import com.expectra.roombooking.dto.BrandDTO;
import com.expectra.roombooking.dto.ChainDTO;
import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Brand;
import com.expectra.roombooking.model.Chain;
import com.expectra.roombooking.service.ChainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/chain")
@Tag(name = "Chain Management", description = "Endpoints para gestión de chains para hoteles")

@CrossOrigin(origins = "*")
public class ChainController {

    private final ChainService chainService;
    private final String messageNotFound = "Chain not found with ID: ";

    @Autowired
    public ChainController(final ChainService chainService) {
        this.chainService = chainService;
    }

    @PostMapping
    @Operation(summary = "Crea una Chain", description = "Crea una Chain usando su ID.")
    public ResponseEntity<ChainDTO> createChain(@RequestBody Map<String, Object> requestBody) {
        Chain chain = new Chain();
        chain.setChainName((String) requestBody.get("chainName"));
        chain.setChainDescription((String) requestBody.get("chainDescription"));

        ChainDTO createdChain = chainService.createChain(chain);
        return ResponseEntity.ok(createdChain);
    }

    @GetMapping("/{chainId}")
    @Operation(summary = "Obtiene una Chain", description = "Recupera una Chain usando su ID.")
    public ResponseEntity<ChainDTO> getChainById(@PathVariable Long chainId) {
        Optional<ChainDTO> chainDTO = chainService.getChainById(chainId);
        return chainDTO.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(messageNotFound + chainId));
    }

    @GetMapping
    @Operation(summary = "Obtiene todos los Chains", description = "Recupera todos los Chains de la base de datos.")
    public ResponseEntity<List<ChainDTO>> getAllChains() {
        List<ChainDTO> chains = chainService.getAllChains();
        return ResponseEntity.ok(chains);
    }

    @PutMapping("/{chainId}")
    @Operation(summary = "Actualiza una Chain", description = "Actualiza los datos de una Chain existente usando su Id.")
    public ResponseEntity<ChainDTO> updateChain(@PathVariable Long chainId, @RequestBody Chain chainDetails) {
        ChainDTO updatedChain = chainService.updateChain(chainId, chainDetails);
        return ResponseEntity.ok(updatedChain);
    }

    @DeleteMapping("/{chainId}")
    @Operation(summary = "Elimina una Chain", description = "Elimina una Chain existente usando su Id.")
    public ResponseEntity<Void> deleteChain(@PathVariable Long chainId) {
        chainService.deleteChain(chainId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{chainId}/brands")
    @Operation(summary = "Obtiene todos los brands de una  chain", description = "Recupera todos los Chains de la base de datos.")
    public ResponseEntity<List<BrandDTO>> getAllBrandsByChainId(@PathVariable Long chainId) {
        System.out.println("chainId = " + chainId);
        List<BrandDTO> brand = chainService.getAllBrandsByChainId(chainId);
        System.out.println("salida");
        return ResponseEntity.ok(brand);
    }
}