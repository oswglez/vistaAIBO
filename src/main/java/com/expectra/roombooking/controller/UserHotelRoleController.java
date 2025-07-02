package com.expectra.roombooking.controller;

import com.expectra.roombooking.dto.UserHotelRoleDTO;
import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.model.UserHotelRole;
import com.expectra.roombooking.service.UserHotelRoleService;
import com.expectra.roombooking.repository.UserRepository;
import com.expectra.roombooking.repository.HotelRepository;
import com.expectra.roombooking.repository.RoleRepository;
import com.expectra.roombooking.repository.UserHotelRoleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user-hotel-roles")
public class UserHotelRoleController {

    private final UserHotelRoleService userHotelRoleService;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final RoleRepository roleRepository;
    private final UserHotelRoleRepository userHotelRoleRepository;

    public UserHotelRoleController(
        UserHotelRoleService userHotelRoleService,
        UserRepository userRepository,
        HotelRepository hotelRepository,
        RoleRepository roleRepository,
        UserHotelRoleRepository userHotelRoleRepository
    ) {
        this.userHotelRoleService = userHotelRoleService;
        this.userRepository = userRepository;
        this.hotelRepository = hotelRepository;
        this.roleRepository = roleRepository;
        this.userHotelRoleRepository = userHotelRoleRepository;
    }

    // --- Métodos de mapeo ---
    private UserHotelRoleDTO toDTO(UserHotelRole uhr) {
        UserHotelRoleDTO dto = new UserHotelRoleDTO();
        dto.setUserHotelRoleId(uhr.getUserHotelRoleId());
        if (uhr.getUser() != null) {
            dto.setUserId(uhr.getUser().getUserId());
            dto.setUsername(uhr.getUser().getUsername());
        }
        if (uhr.getHotel() != null) {
            dto.setHotelId(uhr.getHotel().getHotelId());
            dto.setHotelName(uhr.getHotel().getHotelName());
        }
        if (uhr.getRole() != null) {
            dto.setRoleId(uhr.getRole().getRoleId());
            dto.setRoleName(uhr.getRole().getRoleName());
        }
        if (uhr.getAssignedBy() != null) {
            dto.setAssignedById(uhr.getAssignedBy().getUserId());
            dto.setAssignedByUsername(uhr.getAssignedBy().getUsername());
        }
        dto.setAssignedAt(uhr.getAssignedAt());
        dto.setIsActive(uhr.getIsActive());
        return dto;
    }


    private UserHotelRole toEntity(UserHotelRoleDTO dto, boolean isUpdate) {
        UserHotelRole uhr = new UserHotelRole();
        if (isUpdate && dto.getUserHotelRoleId() != null) {
            uhr.setUserHotelRoleId(dto.getUserHotelRoleId());
        }
        if (dto.getUserId() != null) {
            uhr.setUser(userRepository.findById(dto.getUserId()).orElse(null));
        }
        if (dto.getHotelId() != null) {
            Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found with id: " + dto.getHotelId()));
            uhr.setHotel(hotel);
        }
        if (dto.getRoleId() != null) {
            uhr.setRole(roleRepository.findById(dto.getRoleId()).orElse(null));
        }
        if (dto.getAssignedById() != null) {
            uhr.setAssignedBy(userRepository.findById(dto.getAssignedById()).orElse(null));
        }
        uhr.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        return uhr;
    }

    @GetMapping
    public List<UserHotelRoleDTO> getAll() {
        return userHotelRoleService.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserHotelRoleDTO> getById(@PathVariable Long id) {
        Optional<UserHotelRole> uhr = userHotelRoleService.findById(id);
        return uhr.map(entity -> ResponseEntity.ok(toDTO(entity)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserHotelRoleDTO> create(@RequestBody UserHotelRoleDTO dto) {
        if (userHotelRoleRepository.findByUserUserIdAndHotelHotelIdAndRoleRoleId(
                dto.getUserId(), dto.getHotelId(), dto.getRoleId()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una relación para ese usuario, hotel y rol.");
        }
        UserHotelRole entity = toEntity(dto, false);
        return ResponseEntity.ok(toDTO(userHotelRoleService.save(entity)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserHotelRoleDTO> update(@PathVariable Long id, @RequestBody UserHotelRoleDTO dto) {
        UserHotelRole entity = toEntity(dto, true);
        return userHotelRoleService.update(id, entity)
                .map(updated -> ResponseEntity.ok(toDTO(updated)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (userHotelRoleService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
} 