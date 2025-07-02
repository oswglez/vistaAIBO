package com.expectra.roombooking.service;

import com.expectra.roombooking.model.UserHotelRole;
import com.expectra.roombooking.repository.UserHotelRoleRepository;
import com.expectra.roombooking.repository.UserRepository;
import com.expectra.roombooking.repository.HotelRepository;
import com.expectra.roombooking.repository.RoleRepository;
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

    @Autowired
    public UserHotelRoleServiceImpl(
            UserHotelRoleRepository userHotelRoleRepository,
            UserRepository userRepository,
            HotelRepository hotelRepository,
            RoleRepository roleRepository
    ) {
        this.userHotelRoleRepository = userHotelRoleRepository;
        this.userRepository = userRepository;
        this.hotelRepository = hotelRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void assignRole(Long userId, Long hotelId, Long roleId, Long assignedBy) {
        UserHotelRole uhr = userHotelRoleRepository
            .findByUserUserIdAndHotelHotelIdAndRoleRoleId(userId, hotelId, roleId)
            .orElse(new UserHotelRole());
        uhr.setUser(userRepository.findById(userId).orElseThrow());
        uhr.setHotel(hotelRepository.findById(hotelId).orElseThrow());
        uhr.setRole(roleRepository.findById(roleId).orElseThrow());
        if (assignedBy != null) {
            uhr.setAssignedBy(userRepository.findById(assignedBy).orElse(null));
        }
        uhr.setIsActive(true);
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
} 