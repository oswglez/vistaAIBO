package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    Optional<Role> findByRoleName(String roleName);
    
    boolean existsByRoleName(String roleName);
} 