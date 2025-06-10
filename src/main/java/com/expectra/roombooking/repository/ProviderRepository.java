package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.model.Provider;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProviderRepository extends JpaRepository<Provider, Long> {
    List<Provider> findByProviderType(String providerType);

    @Query("SELECT h FROM Provider p JOIN p.hotels h WHERE p.providerId = :providerId")
    List<Hotel> findAllHotelsByProviderId(@Param("providerId") @NonNull Long providerId);
}
