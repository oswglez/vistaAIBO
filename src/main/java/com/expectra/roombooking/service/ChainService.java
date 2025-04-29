package com.expectra.roombooking.service;

import com.expectra.roombooking.dto.BrandDTO;
import com.expectra.roombooking.dto.HotelDTO;
import com.expectra.roombooking.model.Brand;
import com.expectra.roombooking.model.Chain;

import java.util.List;
import java.util.Optional;

public interface ChainService {
    // Operaciones básicas CRUD
    Chain  createChain(Chain chain);
    Optional<Chain> getChainById(Long chainId);
    List<Chain> getAllChains();
    Chain updateChain(Long chainId, Chain chainDetails);
    void deleteChain(Long chainId);
    List<BrandDTO> getAllBrandsByChainId(Long chainId);
}