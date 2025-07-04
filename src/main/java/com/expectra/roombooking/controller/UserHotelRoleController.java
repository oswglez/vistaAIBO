package com.expectra.roombooking.controller;

import com.expectra.roombooking.dto.UserHotelRoleDTO;
import com.expectra.roombooking.dto.AssignUserRoleDTO;
import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.model.UserHotelRole;
import com.expectra.roombooking.service.UserHotelRoleService;
import com.expectra.roombooking.repository.UserRepository;
import com.expectra.roombooking.repository.HotelRepository;
import com.expectra.roombooking.repository.RoleRepository;
import com.expectra.roombooking.repository.UserHotelRoleRepository;
import com.expectra.roombooking.repository.BrandRepository;
import com.expectra.roombooking.repository.ChainRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/user-hotel-roles")
public class UserHotelRoleController {

    private final UserHotelRoleService userHotelRoleService;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final RoleRepository roleRepository;
    private final UserHotelRoleRepository userHotelRoleRepository;
    private final BrandRepository brandRepository;
    private final ChainRepository chainRepository;

    public UserHotelRoleController(
        UserHotelRoleService userHotelRoleService,
        UserRepository userRepository,
        HotelRepository hotelRepository,
        RoleRepository roleRepository,
        UserHotelRoleRepository userHotelRoleRepository,
        BrandRepository brandRepository,
        ChainRepository chainRepository
    ) {
        this.userHotelRoleService = userHotelRoleService;
        this.userRepository = userRepository;
        this.hotelRepository = hotelRepository;
        this.roleRepository = roleRepository;
        this.userHotelRoleRepository = userHotelRoleRepository;
        this.brandRepository = brandRepository;
        this.chainRepository = chainRepository;
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
        if (uhr.getBrand() != null) {
            dto.setBrandId(uhr.getBrand().getBrandId());
            dto.setBrandName(uhr.getBrand().getBrandName());
        }
        if (uhr.getChain() != null) {
            dto.setChainId(uhr.getChain().getChainId());
            dto.setChainName(uhr.getChain().getChainName());
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
        if (dto.getChainId() != null) {
            uhr.setChain(chainRepository.findById(dto.getChainId()).orElse(null));
        }
        if (dto.getBrandId() != null) {
            uhr.setBrand(brandRepository.findById(dto.getBrandId()).orElse(null));
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
        // Validate that only one field is present
        int count = 0;
        if (dto.getHotelId() != null) count++;
        if (dto.getBrandId() != null) count++;
        if (dto.getChainId() != null) count++;
        if (count != 1) {
            throw new IllegalArgumentException("Must specify only one of: hotelId, brandId or chainId");
        }
        
        // Check for duplicates based on assignment type
        if (dto.getHotelId() != null) {
            if (userHotelRoleRepository.findByUserUserIdAndHotelHotelIdAndRoleRoleId(
                    dto.getUserId(), dto.getHotelId(), dto.getRoleId()).isPresent()) {
                throw new IllegalArgumentException("A relationship already exists for this user, hotel and role.");
            }
        } else if (dto.getBrandId() != null) {
            // Check if a relationship already exists for the user and role in the same brand
            List<UserHotelRole> existingRoles = userHotelRoleRepository.findByUserId(dto.getUserId());
            boolean exists = existingRoles.stream()
                .anyMatch(uhr -> uhr.getBrand() != null && 
                               uhr.getBrand().getBrandId().equals(dto.getBrandId()) && 
                               uhr.getRole().getRoleId().equals(dto.getRoleId()));
            if (exists) {
                throw new IllegalArgumentException("A relationship already exists for this user, brand and role.");
            }
        } else if (dto.getChainId() != null) {
            // Check if a relationship already exists for the user and role in the same chain
            List<UserHotelRole> existingRoles = userHotelRoleRepository.findByUserId(dto.getUserId());
            boolean exists = existingRoles.stream()
                .anyMatch(uhr -> uhr.getChain() != null && 
                               uhr.getChain().getChainId().equals(dto.getChainId()) && 
                               uhr.getRole().getRoleId().equals(dto.getRoleId()));
            if (exists) {
                throw new IllegalArgumentException("A relationship already exists for this user, chain and role.");
            }
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

    @GetMapping("/user/{userId}")
    public List<UserHotelRoleDTO> getUserHotelRolesByUserId(@PathVariable Long userId) {
        return userHotelRoleService.findByUserId(userId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @PostMapping("/assign")
    public ResponseEntity<Map<String, Object>> assignUserRole(@RequestBody AssignUserRoleDTO dto) {
        userHotelRoleService.assignRole(dto);
        return ResponseEntity.ok(Collections.emptyMap());
    }
} 