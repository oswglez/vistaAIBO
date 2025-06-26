package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.UserHotelRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserHotelRoleRepository extends JpaRepository<UserHotelRole, Long> {

    Optional<UserHotelRole> findByUserUserIdAndHotelHotelIdAndRoleRoleId(Long userId, Long hotelId, Long roleId);
    
    @Query("SELECT uhr FROM UserHotelRole uhr WHERE uhr.user.userId = :userId AND uhr.isActive = true")
    List<UserHotelRole> findByUserIdAndIsActiveTrue(@Param("userId") Long userId);
    
    @Query("SELECT uhr FROM UserHotelRole uhr WHERE uhr.hotel.hotelId = :hotelId AND uhr.isActive = true")
    List<UserHotelRole> findByHotelIdAndIsActiveTrue(@Param("hotelId") Long hotelId);
    
    @Query("SELECT uhr FROM UserHotelRole uhr WHERE uhr.user.userId = :userId AND uhr.hotel.hotelId = :hotelId AND uhr.isActive = true")
    List<UserHotelRole> findByUserIdAndHotelIdAndIsActiveTrue(@Param("userId") Long userId, @Param("hotelId") Long hotelId);
} 