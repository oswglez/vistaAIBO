package com.expectra.roombooking.repository;


import com.expectra.roombooking.model.MediaTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface MediaTypesRepository extends JpaRepository<MediaTypes, Long> {

}
