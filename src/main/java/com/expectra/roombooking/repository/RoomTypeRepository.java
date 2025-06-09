package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.RoomTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface RoomTypeRepository extends JpaRepository<RoomTypes, Long> {

}
