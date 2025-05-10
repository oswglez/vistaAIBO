package com.expectra.roombooking.service;

import com.expectra.roombooking.dto.BrandDTO;
import com.expectra.roombooking.dto.ChainDTO;
import com.expectra.roombooking.dto.HotelDTO;
import com.expectra.roombooking.model.Brand;
import com.expectra.roombooking.model.Chain;

import java.util.List;
import java.util.Optional;

public interface ChainService {
    ChainDTO createChain(Chain chain); // Return ChainDTO
    Optional<ChainDTO> getChainById(Long chainId); // Return Optional<ChainDTO>
    List<ChainDTO> getAllChains(); // Return List<ChainDTO>
    ChainDTO updateChain(Long chainId, Chain chainDetails); // Return ChainDTO
    // Operaciones básicas CRUD

    void deleteChain(Long chainId);
    List<BrandDTO> getAllBrandsByChainId(Long chainId);
}