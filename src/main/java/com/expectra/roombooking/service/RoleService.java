package com.expectra.roombooking.service;

import com.expectra.roombooking.model.Role;
import java.util.List;
import java.util.Optional;

public interface RoleService {
    
    List<Role> findAll();
    
    Optional<Role> findById(Long id);
    
    Optional<Role> findByRoleName(String roleName);
    
    Role save(Role role);
    
    Optional<Role> update(Long id, Role role);
    
    boolean delete(Long id);
    
    boolean existsByRoleName(String roleName);
    
    boolean existsById(Long id);
} 