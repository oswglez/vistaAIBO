package com.expectra.roombooking.service;

import com.expectra.roombooking.model.UserHotelRole;
import com.expectra.roombooking.repository.UserHotelRoleRepository;
import com.expectra.roombooking.repository.UserRepository;
import com.expectra.roombooking.repository.HotelRepository;
import com.expectra.roombooking.repository.RoleRepository;
import com.expectra.roombooking.dto.AssignUserRoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserHotelRoleService {

    void assignRole(AssignUserRoleDTO dto);
    java.util.List<UserHotelRole> findAll();
    java.util.Optional<UserHotelRole> findById(Long id);
    UserHotelRole save(UserHotelRole userHotelRole);
    java.util.Optional<UserHotelRole> update(Long id, UserHotelRole userHotelRole);
    boolean delete(Long id);
    List<UserHotelRole> findByUserId(Long userId);
}