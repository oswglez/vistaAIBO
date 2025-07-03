package com.expectra.roombooking.controller;

import com.expectra.roombooking.dto.RoleDTO;
import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Role;
import com.expectra.roombooking.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // --- Métodos de mapeo ---
    private RoleDTO toDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setRoleId(role.getRoleId());
        dto.setRoleName(role.getRoleName());
        dto.setDescription(role.getDescription());
        dto.setPermissions(role.getPermissions());
        return dto;
    }

    private Role toEntity(RoleDTO dto) {
        Role role = new Role();
        if (dto.getRoleId() != null) {
            role.setRoleId(dto.getRoleId());
        }
        role.setRoleName(dto.getRoleName());
        role.setDescription(dto.getDescription());
        role.setPermissions(dto.getPermissions());
        return role;
    }

    @GetMapping
    public List<RoleDTO> getAll() {
        return roleService.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public RoleDTO getById(@PathVariable Long id) {
        Role role = roleService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));
        return toDTO(role);
    }

    @PostMapping
    public RoleDTO create(@RequestBody RoleDTO dto) {
        if (roleService.existsByRoleName(dto.getRoleName())) {
            throw new IllegalArgumentException("Ya existe un rol con el nombre: " + dto.getRoleName());
        }
        Role entity = toEntity(dto);
        return toDTO(roleService.save(entity));
    }

    @PutMapping("/{id}")
    public RoleDTO update(@PathVariable Long id, @RequestBody RoleDTO dto) {
        // Verificar que el rol existe antes de actualizar
        if (!roleService.existsById(id)) {
            throw new ResourceNotFoundException("Rol no encontrado con id: " + id);
        }
        
        Role entity = toEntity(dto);
        Role updated = roleService.update(id, entity)
                .orElseThrow(() -> new ResourceNotFoundException("Error al actualizar rol con id: " + id));
        return toDTO(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!roleService.existsById(id)) {
            throw new ResourceNotFoundException("Rol no encontrado con id: " + id);
        }
        
        if (roleService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            throw new IllegalArgumentException("Error al eliminar rol con id: " + id);
        }
    }
} 