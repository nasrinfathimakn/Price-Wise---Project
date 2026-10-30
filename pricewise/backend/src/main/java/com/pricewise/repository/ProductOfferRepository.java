package com.pricewise.repository;

import com.pricewise.entity.ProductOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductOfferRepository extends JpaRepository<ProductOffer, Long> {

    List<ProductOffer> findByProductId(Long productId);

    List<ProductOffer> findByProductIdAndActiveTrue(Long productId);

    Optional<ProductOffer> findByProductIdAndRetailerId(Long productId, Long retailerId);

    Optional<ProductOffer> findByRetailerIdAndExternalProductId(Long retailerId, String externalProductId);

    List<ProductOffer> findByRetailerId(Long retailerId);

    @Query("SELECT o FROM ProductOffer o WHERE o.productId = :productId AND o.active = true ORDER BY o.currentPrice ASC")
    List<ProductOffer> findActiveOffersSortedByPrice(@Param("productId") Long productId);

    @Query("SELECT o FROM ProductOffer o WHERE o.lastFetchedAt < :threshold OR o.lastFetchedAt IS NULL")
    List<ProductOffer> findOffersRequiringRefresh(@Param("threshold") OffsetDateTime threshold);

    @Query("SELECT MIN(o.currentPrice) FROM ProductOffer o WHERE o.productId = :productId AND o.active = true AND o.currentPrice > 0")
    Optional<java.math.BigDecimal> findLowestPriceByProductId(@Param("productId") Long productId);
}

