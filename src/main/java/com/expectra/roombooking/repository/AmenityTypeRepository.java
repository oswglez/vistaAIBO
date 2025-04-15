package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.AmenityType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface AmenityTypeRepository extends CrudRepository<AmenityType, Long> {

}
