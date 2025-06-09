package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.AmenityTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface AmenityTypeRepository extends JpaRepository<AmenityTypes, Long> {

}
