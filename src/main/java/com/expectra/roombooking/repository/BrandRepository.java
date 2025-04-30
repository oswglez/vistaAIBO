package com.expectra.roombooking.repository;

import com.expectra.roombooking.dto.HotelOnlyDTO;
import com.expectra.roombooking.model.Brand;
import com.expectra.roombooking.model.Hotel;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
    public interface BrandRepository extends CrudRepository<Brand, Long> {
    @Query("SELECT b FROM Brand b JOIN FETCH b.hotels WHERE b.brandId = :brandId")
    List<Brand> findAllHotelsByBrandId(@Param("brandId") @NonNull Long brandId);

    @Query("SELECT DISTINCT b FROM Brand b LEFT JOIN FETCH b.hotels WHERE b.brandId = :brandId")
    Optional<Brand> findByIdWithHotels(@Param("brandId") Long brandId);
}
