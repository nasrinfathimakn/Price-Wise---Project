package com.pricewise.service;

import com.pricewise.dto.ProductMatchReviewDTO;
import com.pricewise.entity.Brand;
import com.pricewise.entity.Product;
import com.pricewise.entity.ProductSpecification;
import com.pricewise.integration.ExternalOfferDTO;
import com.pricewise.repository.BrandRepository;
import com.pricewise.repository.ProductRepository;
import com.pricewise.repository.ProductSpecificationRepository;
import com.pricewise.util.ProductNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProductMatchingService {

    private static final Logger log = LoggerFactory.getLogger(ProductMatchingService.class);

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final ProductSpecificationRepository specificationRepository;

    // Stores pending review matches flagged by rule 5
    private final Map<Long, ProductMatchReviewDTO> pendingReviews = new ConcurrentHashMap<>();

    public ProductMatchingService(
            ProductRepository productRepository,
            BrandRepository brandRepository,
            ProductSpecificationRepository specificationRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.specificationRepository = specificationRepository;
    }

    public static class MatchResult {
        private final Optional<Product> matchedProduct;
        private final boolean ambiguous;
        private final String matchReason;
        private final double confidence;

        public MatchResult(Optional<Product> matchedProduct, boolean ambiguous, String matchReason, double confidence) {
            this.matchedProduct = matchedProduct;
            this.ambiguous = ambiguous;
            this.matchReason = matchReason;
            this.confidence = confidence;
        }

        public Optional<Product> getMatchedProduct() {
            return matchedProduct;
        }

        public boolean isAmbiguous() {
            return ambiguous;
        }

        public String getMatchReason() {
            return matchReason;
        }

        public double getConfidence() {
            return confidence;
        }
    }

    /**
     * Executes the 5-tier product matching algorithm:
     * 1. Match exact GTIN, EAN, UPC
     * 2. Match manufacturer and exact model number
     * 3. Compare normalized brand, model, and product attributes (Storage, RAM)
     * 4. Use carefully controlled fuzzy text matching as a fallback
     * 5. Flag uncertain matches for manual review
     */
    public MatchResult matchOfferToProduct(ExternalOfferDTO offer) {
        if (offer == null) {
            return new MatchResult(Optional.empty(), false, "Null offer", 0.0);
        }

        // Priority 1: Exact GTIN / EAN / UPC
        if (StringUtils.hasText(offer.getGtin())) {
            Optional<Product> gtinMatch = productRepository.findByProductIdentifier(offer.getGtin().trim());
            if (gtinMatch.isPresent()) {
                // Check variant guard
                if (validateVariants(gtinMatch.get(), offer)) {
                    log.debug("Matched by GTIN: {}", offer.getGtin());
                    return new MatchResult(gtinMatch, false, "EXACT_GTIN", 1.0);
                }
            }
        }

        // Priority 2: Manufacturer & Exact Model Number
        String rawBrand = offer.getRawBrand();
        String rawModel = offer.getRawModel();
        if (StringUtils.hasText(rawModel)) {
            String normBrand = ProductNormalizer.normalizeBrand(rawBrand);
            Optional<Brand> brand = brandRepository.findByNameIgnoreCase(normBrand);
            if (brand.isPresent()) {
                Optional<Product> modelMatch = productRepository.findByBrandIdAndModelNumber(brand.get().getId(), rawModel.trim());
                if (modelMatch.isPresent() && validateVariants(modelMatch.get(), offer)) {
                    log.debug("Matched by Brand + Model: {} / {}", normBrand, rawModel);
                    return new MatchResult(modelMatch, false, "BRAND_AND_MODEL", 0.95);
                }
            }
        }

        // Priority 3: Compare Normalized Brand, Model & Specifications (Storage, RAM)
        String offerTitle = offer.getExternalTitle();
        String offerStorage = ProductNormalizer.extractStorage(offerTitle);
        String offerRam = ProductNormalizer.extractRam(offerTitle);

        List<Product> allProducts = productRepository.findByActiveTrue();
        for (Product product : allProducts) {
            if (!validateVariants(product, offer)) {
                continue; // Prevent storage/RAM mismatches
            }

            // Check brand match
            if (product.getBrandId() != null) {
                Optional<Brand> brand = brandRepository.findById(product.getBrandId());
                if (brand.isPresent() && !offerTitle.toLowerCase().contains(brand.get().getName().toLowerCase())) {
                    continue; // Brand mismatch
                }
            }

            // Check model substring in title
            if (StringUtils.hasText(product.getModelNumber()) &&
                    offerTitle.toLowerCase().contains(product.getModelNumber().toLowerCase())) {
                return new MatchResult(Optional.of(product), false, "NORMALIZED_ATTRIBUTES", 0.90);
            }
        }

        // Priority 4: Carefully Controlled Fuzzy Text Matching
        Product bestCandidate = null;
        double highestSimilarity = 0.0;

        for (Product product : allProducts) {
            if (!validateVariants(product, offer)) {
                continue;
            }

            double sim = ProductNormalizer.calculateSimilarity(product.getName(), offerTitle);
            if (sim > highestSimilarity) {
                highestSimilarity = sim;
                bestCandidate = product;
            }
        }

        if (bestCandidate != null && highestSimilarity >= 0.85) {
            return new MatchResult(Optional.of(bestCandidate), false, "FUZZY_MATCH_HIGH_CONFIDENCE", highestSimilarity);
        }

        // Priority 5: Flag uncertain matches for manual review instead of auto merging
        if (bestCandidate != null && highestSimilarity >= 0.65) {
            log.warn("Uncertain product match flagged for review: '{}' against '{}' (Score: {})",
                    offerTitle, bestCandidate.getName(), highestSimilarity);
            flagForReview(offer, bestCandidate, highestSimilarity);
            return new MatchResult(Optional.of(bestCandidate), true, "UNCERTAIN_MATCH_PENDING_REVIEW", highestSimilarity);
        }

        return new MatchResult(Optional.empty(), false, "NO_MATCH_FOUND", 0.0);
    }

    /**
     * Strict variant validation rule:
     * A 128 GB phone must never match a 256 GB phone!
     */
    public boolean validateVariants(Product product, ExternalOfferDTO offer) {
        String offerStorage = ProductNormalizer.extractStorage(offer.getExternalTitle());
        String productStorage = ProductNormalizer.extractStorage(product.getName());

        if (offerStorage != null && productStorage != null && !offerStorage.equalsIgnoreCase(productStorage)) {
            return false;
        }

        String offerRam = ProductNormalizer.extractRam(offer.getExternalTitle());
        String productRam = ProductNormalizer.extractRam(product.getName());
        if (offerRam != null && productRam != null && !offerRam.equalsIgnoreCase(productRam)) {
            return false;
        }

        return true;
    }

    private void flagForReview(ExternalOfferDTO offer, Product candidate, double score) {
        ProductMatchReviewDTO dto = new ProductMatchReviewDTO();
        long key = System.currentTimeMillis() + (long)(score * 1000);
        dto.setOfferId(key);
        dto.setProposedProductId(candidate.getId());
        dto.setProposedProductName(candidate.getName());
        dto.setRetailerName(offer.getRetailerCode());
        dto.setExternalProductId(offer.getExternalProductId());
        dto.setExternalTitle(offer.getExternalTitle());
        dto.setCurrentPrice(offer.getPrice());
        dto.setMatchReason("Confidence score below automatic threshold (" + Math.round(score * 100) + "%)");
        dto.setConfidenceScore(score);
        dto.setResolutionStatus("PENDING");
        pendingReviews.put(key, dto);
    }

    public List<ProductMatchReviewDTO> getPendingReviews() {
        return pendingReviews.values().stream()
                .filter(r -> "PENDING".equalsIgnoreCase(r.getResolutionStatus()))
                .toList();
    }

    public boolean resolveMatch(Long matchId, boolean approve) {
        ProductMatchReviewDTO review = pendingReviews.get(matchId);
        if (review != null) {
            review.setResolutionStatus(approve ? "APPROVED" : "REJECTED");
            pendingReviews.remove(matchId);
            return true;
        }
        return false;
    }
}

