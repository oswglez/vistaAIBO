package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.FloorPlan;
import com.expectra.roombooking.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FloorPlanRepository extends JpaRepository<FloorPlan, Long> {

    // JPA proporciona automáticamente métodos como findAll(), findById(), save(), delete(), etc.
    // Si necesitas encontrar planos por hotel, puedes agregar:
//    @Query("SELECT m FROM Media m JOIN m.hotels h WHERE h.hotelId = :hotelId")
    @Query("SELECT f FROM FloorPlan f WHERE f.hotel.hotelId = :hotelId")
    List<FloorPlan> findAllFloorPlansByHotelId(@Param("hotelId") Long hotelId);
}
