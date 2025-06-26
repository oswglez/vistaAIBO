package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByAuth0Id(String auth0Id);
    
    Optional<User> findByUsernameAndIsActiveTrue(String username);
    
    List<User> findByIsActiveTrue();
    
    @Query("SELECT u FROM User u WHERE u.username = :username OR u.email = :email")
    Optional<User> findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
} 