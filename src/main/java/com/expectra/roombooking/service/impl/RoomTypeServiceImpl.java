package com.expectra.roombooking.service.impl;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.MediaTypes;
import com.expectra.roombooking.model.RoomTypes;
import com.expectra.roombooking.repository.RoomTypeRepository;
import com.expectra.roombooking.service.RoomTypeService;
import com.expectra.roombooking.service.RoomTypeValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final RoomTypeValidationService roomTypeValidator;


    @Autowired
    public RoomTypeServiceImpl(RoomTypeRepository roomTypeRepository, RoomTypeValidationService roomTypeValidator) {
        this.roomTypeRepository = roomTypeRepository;
        this.roomTypeValidator = roomTypeValidator;
    }

    @Override
    @Transactional
    public RoomTypes createRoomType(RoomTypes roomTypes) {
        return roomTypeRepository.save(roomTypes);
    }

    @Override
    public Optional<RoomTypes> getRoomTypeById(Long id) {
        return roomTypeRepository.findById(id);
    }

    @Override
    public Page<RoomTypes> getAllRoomType(Pageable pageable) {
        return roomTypeRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public RoomTypes updateRoomType(Long id, RoomTypes roomTypeDetails) {
        roomTypeValidator.validateType(roomTypeDetails.getRoomTypeName());

        RoomTypes roomTypes = roomTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RoomType not found with id: " + id));

        roomTypes.setRoomTypeDescription(roomTypeDetails.getRoomTypeDescription());
        roomTypes.setRoomTypeName(roomTypeDetails.getRoomTypeName());
        return roomTypeRepository.save(roomTypes);
    }

    @Override
    @Transactional
    public void deleteRoomType(Long id) {
        RoomTypes roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RoomType not found with id: " + id));
        roomTypeRepository.delete(roomType);
    }
}