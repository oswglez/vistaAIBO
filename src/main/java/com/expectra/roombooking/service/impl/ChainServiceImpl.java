package com.expectra.roombooking.service.impl;

import com.expectra.roombooking.dto.BrandDTO;
import com.expectra.roombooking.dto.ChainDTO;
import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Brand;
import com.expectra.roombooking.model.Chain;
import com.expectra.roombooking.repository.ChainRepository;
import com.expectra.roombooking.service.ChainService;
import com.expectra.roombooking.service.ChainValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChainServiceImpl implements ChainService {

    private final ChainRepository chainRepository;
    private final ChainValidationService chainValidator;
    private final ModelMapper modelMapper; // Add ModelMapper

    @Autowired
    public ChainServiceImpl(ChainRepository chainRepository, ChainValidationService chainValidator, ModelMapper modelMapper) { // Inject ModelMapper
        this.chainRepository = chainRepository;
        this.chainValidator = chainValidator;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public ChainDTO createChain(Chain chain) {
        Chain savedChain = chainRepository.save(chain);
        return modelMapper.map(savedChain, ChainDTO.class);
    }

    @Override
    public Optional<ChainDTO> getChainById(Long chainId) {
        return chainRepository.findById(chainId)
                .map(chain -> modelMapper.map(chain, ChainDTO.class));
    }

    @Override
    public List<ChainDTO> getAllChains() {
        List<Chain> chains = (List<Chain>) chainRepository.findAll();
        return chains.stream()
                .map(chain -> modelMapper.map(chain, ChainDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChainDTO updateChain(Long chainId, Chain chainDetails) {
        chainValidator.validateName(chainDetails.getChainName());

        Chain chain = chainRepository.findById(chainId)
                .orElseThrow(() -> new ResourceNotFoundException("Chain not found with id: " + chainId));

        chain.setChainDescription(chainDetails.getChainDescription());
        Chain updatedChain = chainRepository.save(chain);
        return modelMapper.map(updatedChain, ChainDTO.class);
    }

    @Override
    @Transactional
    public void deleteChain(Long chainId) {
        Chain chain = chainRepository.findById(chainId)
                .orElseThrow(() -> new ResourceNotFoundException("Chain not found with id: " + chainId));
        chainRepository.delete(chain);
    }
    @Override
    @Transactional
    public List<BrandDTO> getAllBrandsByChainId(Long chainId) {
        if (chainRepository.findById(chainId).isEmpty()) {
            throw new ResourceNotFoundException("Chain not found with id: " + chainId);
        };
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
}