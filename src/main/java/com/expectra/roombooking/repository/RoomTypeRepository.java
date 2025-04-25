package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.RoomTypes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface RoomTypeRepository extends CrudRepository<RoomTypes, Long> {

}
