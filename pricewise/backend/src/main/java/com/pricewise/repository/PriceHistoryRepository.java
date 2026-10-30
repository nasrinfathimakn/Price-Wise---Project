package com.pricewise.repository;

import com.pricewise.entity.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {

    List<PriceHistory> findByProductOfferIdOrderByObservedAtAsc(Long productOfferId);

    List<PriceHistory> findByProductOfferIdAndObservedAtBetweenOrderByObservedAtAsc(
            Long productOfferId,
            OffsetDateTime start,
            OffsetDateTime end
    );

    @Query("SELECT ph FROM PriceHistory ph WHERE ph.productOfferId IN :offerIds ORDER BY ph.observedAt ASC")
    List<PriceHistory> findByProductOfferIdInOrderByObservedAtAsc(@Param("offerIds") List<Long> offerIds);

    @Query("SELECT MIN(ph.price) FROM PriceHistory ph WHERE ph.productOfferId = :offerId")
    Optional<BigDecimal> findLowestPriceByOfferId(@Param("offerId") Long offerId);

    @Query("SELECT MAX(ph.price) FROM PriceHistory ph WHERE ph.productOfferId = :offerId")
    Optional<BigDecimal> findHighestPriceByOfferId(@Param("offerId") Long offerId);

    @Query("SELECT ph.price FROM PriceHistory ph WHERE ph.productOfferId = :offerId ORDER BY ph.observedAt DESC LIMIT 1")
    Optional<BigDecimal> findLatestPriceByOfferId(@Param("offerId") Long offerId);
}

