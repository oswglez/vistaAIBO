package com.expectra.roombooking.service.impl;

import com.expectra.roombooking.dto.BrandDTO;
import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Brand;
import com.expectra.roombooking.model.Chain;
import com.expectra.roombooking.repository.ChainRepository;
import com.expectra.roombooking.service.ChainService;
import com.expectra.roombooking.service.ChainValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ChainServiceImpl implements ChainService {

    private final ChainRepository chainRepository;
    private final ChainValidationService chainValidator;

    @Autowired
    public ChainServiceImpl(ChainRepository chainRepository, ChainValidationService chainValidator) {
        this.chainRepository = chainRepository;
        this.chainValidator = chainValidator;
    }

    @Override
    @Transactional
    public Chain createChain(Chain chain) {
        return chainRepository.save(chain);
    }

    @Override
    public Optional<Chain> getChainById(Long chainId) {
        return chainRepository.findById(chainId);
    }
//    @Override
//    @Transactional
//    public  List<Brand> getAllBrandsByChainId(Long chainId) {
//        return chainRepository.getAllBrandsByChainId(chainId);
//
//    }
    @Override
    @Transactional
    public List<BrandDTO> getAllBrandsByChainId(Long chainId) {
        List<Brand> brands = chainRepository.getAllBrandsByChainId(chainId);
        return brands.stream()
                .map(brand -> {
                    BrandDTO dto = new BrandDTO();
                    dto.setBrandId(brand.getBrandId());
                    dto.setBrandName(brand.getBrandName());
                    return dto;
                })
                .toList();
    }
    @Override
    public List<Chain> getAllChains() {
        return (List<Chain>) chainRepository.findAll();
    }

    @Override
    @Transactional
    public Chain updateChain(Long chainId, Chain ChainsDetails) {
        chainValidator.validateName(ChainsDetails.getChainName());

        Chain chain = chainRepository.findById(chainId)
                .orElseThrow(() -> new ResourceNotFoundException("Chain not found with id: " + chainId));

        chain.setChainDescription(ChainsDetails.getChainDescription());
        return chainRepository.save(chain);
    }

    @Override
    @Transactional
    public void deleteChain(Long chainId) {
        Chain chain = chainRepository.findById(chainId)
                .orElseThrow(() -> new ResourceNotFoundException("Chain not found with id: " + chainId));
        chainRepository.delete(chain);
    }
}