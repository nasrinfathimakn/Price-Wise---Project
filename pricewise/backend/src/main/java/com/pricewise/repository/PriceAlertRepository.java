package com.pricewise.repository;

import com.pricewise.entity.PriceAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {
    List<PriceAlert> findByUserId(Long userId);
    List<PriceAlert> findByProductIdAndActiveTrue(Long productId);
    List<PriceAlert> findByActiveTrue();
    Optional<PriceAlert> findByIdAndUserId(Long id, Long userId);
}

