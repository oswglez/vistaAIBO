package com.expectra.roombooking.controller;

import com.expectra.roombooking.dto.BrandDTO;
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

@RestController
@RequestMapping("/api/chain")
@Tag(name = "Chain Management", description = "Endpoints para gestión de chains para hoteles")

@CrossOrigin(origins = "*")
public class ChainController {

    private final ChainService chainService;
    private final String messageNotFound = "Room not found with ID: ";

    @Autowired
    public ChainController(final ChainService chainService) {
        this.chainService = chainService;
    }

    @PostMapping
    @Operation(summary = "Crea una Chain", description = "Crea una Chain usando su ID.")
    public ResponseEntity<Chain> createChain(@RequestBody Map<String, Object> requestBody) {
        Chain chain = new Chain();
        chain.setChainName((String) requestBody.get("chainName"));
        chain.setChainDescription((String) requestBody.get("chainDescription"));

        Chain createdChain = chainService.createChain(chain);
        return ResponseEntity.ok(createdChain);
    }

    @GetMapping("/{chainId}")
    @Operation(summary = "Obtiene una Chain", description = "Recupera una Chain usando su ID.")
    public ResponseEntity<Chain> getChainByIdById(@PathVariable Long chainId) {
        return chainService.getChainById(chainId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(messageNotFound + chainId));
    }

    @GetMapping
    @Operation(summary = "Obtiene todos los Chains", description = "Recupera todos los Chains de la base de datos.")
    public ResponseEntity<List<Chain>> getAllChains() {
        List<Chain> chains = chainService.getAllChains();
        return ResponseEntity.ok(chains);
    }

    @GetMapping("/{chainId}/brands")
    @Operation(summary = "Obtiene todos los brands de una  chain", description = "Recupera todos los Chains de la base de datos.")
    public ResponseEntity<List<BrandDTO>> getAllBrandsByChainId(@PathVariable Long chainId) {
        System.out.println("chainId = " + chainId);
        List<BrandDTO> brand = chainService.getAllBrandsByChainId(chainId);
        System.out.println("salida");
        return ResponseEntity.ok(brand);
    }

    @PutMapping("/{chainId}")
    @Operation(summary = "Actualiza una Chain", description = "Actualiza los datos de una Chain existente usando su Id.")
    public ResponseEntity<Chain> updateChain(@PathVariable Long chainId, @RequestBody Chain ChainsDetails) {
        Chain updatedChain = chainService.updateChain(chainId, ChainsDetails);
        return ResponseEntity.ok(updatedChain);
    }

    @DeleteMapping("/{chainId}")
    @Operation(summary = "Elimina una Chain", description = "Elimina una Chain existente usando su Id.")
    public ResponseEntity<Chain> deleteChain(@PathVariable Long chainId) {
        chainService.deleteChain(chainId);
        return ResponseEntity.noContent().build();
    }
}