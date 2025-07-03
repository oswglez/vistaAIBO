package com.expectra.roombooking.service;

import com.expectra.roombooking.model.UserHotelRole;
import com.expectra.roombooking.repository.UserHotelRoleRepository;
import com.expectra.roombooking.repository.UserRepository;
import com.expectra.roombooking.repository.HotelRepository;
import com.expectra.roombooking.repository.RoleRepository;
import com.expectra.roombooking.repository.BrandRepository;
import com.expectra.roombooking.repository.ChainRepository;
import com.expectra.roombooking.dto.AssignUserRoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserHotelRoleServiceImpl implements UserHotelRoleService {

    private final UserHotelRoleRepository userHotelRoleRepository;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final RoleRepository roleRepository;
    private final BrandRepository brandRepository;
    private final ChainRepository chainRepository;

    @Autowired
    public UserHotelRoleServiceImpl(
            UserHotelRoleRepository userHotelRoleRepository,
            UserRepository userRepository,
            HotelRepository hotelRepository,
            RoleRepository roleRepository,
            BrandRepository brandRepository,
            ChainRepository chainRepository
    ) {
        this.userHotelRoleRepository = userHotelRoleRepository;
        this.userRepository = userRepository;
        this.hotelRepository = hotelRepository;
        this.roleRepository = roleRepository;
        this.brandRepository = brandRepository;
        this.chainRepository = chainRepository;
    }

    @Override
    public void assignRole(AssignUserRoleDTO dto) {
        // Validate that only one field is present
        int count = 0;
        if (dto.getHotelId() != null) count++;
        if (dto.getBrandId() != null) count++;
        if (dto.getChainId() != null) count++;
        if (count != 1) {
            throw new IllegalArgumentException("Must specify only one of: hotelId, brandId or chainId");
        }

        UserHotelRole uhr = new UserHotelRole();
        uhr.setUser(userRepository.findById(dto.getUserId()).orElseThrow());
        uhr.setRole(roleRepository.findById(dto.getRoleId()).orElseThrow());
        if (dto.getHotelId() != null) {
            uhr.setHotel(hotelRepository.findById(dto.getHotelId()).orElseThrow());
        }
        if (dto.getBrandId() != null) {
            uhr.setBrand(brandRepository.findById(dto.getBrandId()).orElseThrow());
        }
        if (dto.getChainId() != null) {
            uhr.setChain(chainRepository.findById(dto.getChainId()).orElseThrow());
        }
        if (dto.getAssignedBy() != null) {
            uhr.setAssignedBy(userRepository.findById(dto.getAssignedBy()).orElse(null));
        }
        uhr.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        uhr.setAssignedAt(LocalDateTime.now());
        userHotelRoleRepository.save(uhr);
    }

    @Override
    public List<UserHotelRole> findAll() {
        return userHotelRoleRepository.findAll();
    }

    @Override
    public Optional<UserHotelRole> findById(Long id) {
        return userHotelRoleRepository.findById(id);
    }

    @Override
    public UserHotelRole save(UserHotelRole userHotelRole) {
        return userHotelRoleRepository.save(userHotelRole);
    }

    @Override
    public Optional<UserHotelRole> update(Long id, UserHotelRole userHotelRole) {
        return userHotelRoleRepository.findById(id).map(existing -> {
            userHotelRole.setUserHotelRoleId(id);
            return userHotelRoleRepository.save(userHotelRole);
        });
    }

    @Override
    public boolean delete(Long id) {
        if (userHotelRoleRepository.existsById(id)) {
            userHotelRoleRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<UserHotelRole> findByUserId(Long userId) {
        return userHotelRoleRepository.findByUserId(userId);
    }
} 