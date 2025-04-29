package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Brand;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface BrandRepository extends CrudRepository<Brand, Long> {

}
