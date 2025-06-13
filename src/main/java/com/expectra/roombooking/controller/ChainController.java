package com.expectra.roombooking.controller;

import com.expectra.roombooking.dto.BrandDTO;
import com.expectra.roombooking.dto.ChainDTO;
import com.expectra.roombooking.exception.ResourceNotFoundException;
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
@Tag(name = "Chain Management", description = "Endpoints for managing hotel chains")

@CrossOrigin(origins = "*")
public class ChainController {

    private final ChainService chainService;
    private final String messageNotFound = "Chain not found with ID: ";

    @Autowired
    public ChainController(final ChainService chainService) {
        this.chainService = chainService;
    }

    @PostMapping
    @Operation(summary = "Create a chain", description = "Creates a chain using its ID.")
    public ResponseEntity<ChainDTO> createChain(@RequestBody Map<String, Object> requestBody) {
        Chain chain = new Chain();
        chain.setChainName((String) requestBody.get("chainName"));
        chain.setChainDescription((String) requestBody.get("chainDescription"));

        ChainDTO createdChain = chainService.createChain(chain);
        return ResponseEntity.ok(createdChain);
    }

    @GetMapping("/{chainId}")
    @Operation(summary = "Get a chain", description = "Retrieves a chain using its ID.")
    public ResponseEntity<ChainDTO> getChainById(@PathVariable Long chainId) {
        Optional<ChainDTO> chainDTO = chainService.getChainById(chainId);
        return chainDTO.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(messageNotFound + chainId));
    }

    @GetMapping
    @Operation(summary = "Get all chains", description = "Retrieves all chains from the database.")
    public ResponseEntity<List<ChainDTO>> getAllChains() {
        List<ChainDTO> chains = chainService.getAllChains();
        return ResponseEntity.ok(chains);
    }

    @PutMapping("/{chainId}")
    @Operation(summary = "Update a chain", description = "Updates an existing chain using its ID.")
    public ResponseEntity<ChainDTO> updateChain(@PathVariable Long chainId, @RequestBody Chain chainDetails) {
        ChainDTO updatedChain = chainService.updateChain(chainId, chainDetails);
        return ResponseEntity.ok(updatedChain);
    }

    @DeleteMapping("/{chainId}")
    @Operation(summary = "Delete a chain", description = "Deletes an existing chain using its ID.")
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