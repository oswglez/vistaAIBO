package com.expectra.roombooking.service;

import com.expectra.roombooking.dto.BrandDTO;
import com.expectra.roombooking.dto.ChainDTO;
import com.expectra.roombooking.model.Chain;

import java.util.List;
import java.util.Optional;

public interface ChainService {
    // Basic CRUD Operations
    ChainDTO createChain(Chain chain);
    Optional<ChainDTO> getChainById(Long chainId);
    List<ChainDTO> getAllChains();
    ChainDTO updateChain(Long chainId, Chain chainDetails);
    void deleteChain(Long chainId);
    List<BrandDTO> getAllBrandsByChainId(Long chainId);
}