package com.expectra.roombooking.service;

import com.expectra.roombooking.model.FloorPlan;
import com.expectra.roombooking.model.Hotel;

import java.util.List;

public interface FloorPlanService {
    List<FloorPlan> getAllFloorPlans();
    FloorPlan getFloorPlanById(Long id);
    List<FloorPlan> getFloorPlansByHotelId(Long hotelId);
    FloorPlan createFloorPlanForHotel(Long hotelId,  FloorPlan floorPlan);
    FloorPlan saveFloorPlan(FloorPlan floorPlan);
    //void deleteFloorPlan(Long id);
    void deleteFloorPlanById(Long hotelId);
    List<FloorPlan> saveAllFloorPlans(List<FloorPlan> floorPlans);
}