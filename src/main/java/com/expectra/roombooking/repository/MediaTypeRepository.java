package com.expectra.roombooking.repository;


import com.expectra.roombooking.model.MediaType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface MediaTypeRepository extends CrudRepository<MediaType, Long> {

}
