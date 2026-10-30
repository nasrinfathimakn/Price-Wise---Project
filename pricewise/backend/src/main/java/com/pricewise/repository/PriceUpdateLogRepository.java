package com.pricewise.repository;

import com.pricewise.entity.PriceUpdateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceUpdateLogRepository extends JpaRepository<PriceUpdateLog, Long> {
    List<PriceUpdateLog> findAllByOrderByStartedAtDesc();
    List<PriceUpdateLog> findByRetailerIdOrderByStartedAtDesc(Long retailerId);
}

