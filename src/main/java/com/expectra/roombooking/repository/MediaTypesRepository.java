package com.expectra.roombooking.repository;


import com.expectra.roombooking.model.MediaTypes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface MediaTypesRepository extends CrudRepository<MediaTypes, Long> {

}
