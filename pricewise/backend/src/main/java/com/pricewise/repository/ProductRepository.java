package com.pricewise.repository;

import com.pricewise.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductIdentifier(String productIdentifier);

    Optional<Product> findByBrandIdAndModelNumber(Long brandId, String modelNumber);

    List<Product> findByActiveTrue();

    @Query("SELECT p FROM Product p WHERE p.active = true " +
           "AND (:brandId IS NULL OR p.brandId = :brandId) " +
           "AND (:categoryId IS NULL OR p.categoryId = :categoryId) " +
           "AND (:query IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "     OR LOWER(p.modelNumber) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "     OR LOWER(p.productIdentifier) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Product> searchProducts(
            @Param("query") String query,
            @Param("brandId") Long brandId,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );

    @Query("SELECT p.name FROM Product p WHERE p.active = true " +
           "AND LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "ORDER BY p.name ASC")
    List<String> findNameSuggestions(@Param("query") String query, Pageable pageable);

    List<Product> findByCategoryIdAndActiveTrue(Long categoryId);

    List<Product> findByCategoryIdAndIdNotAndActiveTrue(Long categoryId, Long id);
}

