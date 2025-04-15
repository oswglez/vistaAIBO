package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.RoomType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface RoomTypeRepository extends CrudRepository<RoomType, Long> {

}
