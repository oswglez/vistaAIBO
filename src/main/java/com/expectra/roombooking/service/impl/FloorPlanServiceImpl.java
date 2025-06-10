// MediaServiceImpl.java
package com.expectra.roombooking.service.impl;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.FloorPlan;
import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.repository.FloorPlanRepository;
import com.expectra.roombooking.repository.HotelRepository;
import com.expectra.roombooking.service.FloorPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FloorPlanServiceImpl implements FloorPlanService {

    private final FloorPlanRepository floorPlanRepository;
    private final HotelRepository hotelRepository;


    @Autowired
    public FloorPlanServiceImpl(FloorPlanRepository floorPlanRepository, HotelRepository hotelRepository) {
        this.floorPlanRepository = floorPlanRepository;
        this.hotelRepository = hotelRepository;
    }

    @Override
    public List<FloorPlan> getAllFloorPlans() {
        return floorPlanRepository.findAll();
    }

    @Override
    public FloorPlan getFloorPlanById(Long id) {
        return floorPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FloorPlan not found with id: " + id));
    }

    @Override
    public List<FloorPlan> getFloorPlansByHotelId(Long hotelId) {
        return floorPlanRepository.findAllFloorPlansByHotelId(hotelId);
    }

    @Override
    public FloorPlan createFloorPlanForHotel(Long hotelId, FloorPlan floorPlan) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));
        floorPlan.setHotel(hotel);
        return floorPlanRepository.save(floorPlan);
    }

    @Override
    public FloorPlan saveFloorPlan(FloorPlan floorPlan) {
        return floorPlanRepository.save(floorPlan);
    }

    @Override
    public void deleteFloorPlanById(Long floorPlanId) {
        floorPlanRepository.findById(floorPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("FloorPlan not found with ID: " + floorPlanId));

        floorPlanRepository.deleteById(floorPlanId);
    }

//    @Override
//    public void deleteFloorPlan(Long id) {
//        floorPlanRepository.deleteById(id);
//    }

    @Override
    public List<FloorPlan> saveAllFloorPlans(List<FloorPlan> floorPlans) {
        return floorPlanRepository.saveAll(floorPlans);
    }


}