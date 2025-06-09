package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Brand;
import com.expectra.roombooking.model.Chain;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChainRepository extends JpaRepository<Chain, Long> {
    @Query("SELECT b FROM Brand b WHERE b.chain.chainId = :chainId")
    List<Brand> getAllBrandsByChainId(@Param("chainId") @NonNull Long chainId);
}
