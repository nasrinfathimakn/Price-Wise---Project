package com.pricewise.service;

import com.pricewise.config.RetailerConfigProperties;
import com.pricewise.dto.PriceComparisonResponseDTO;
import com.pricewise.dto.ProductOfferResponseDTO;
import com.pricewise.entity.Product;
import com.pricewise.entity.ProductOffer;
import com.pricewise.entity.Retailer;
import com.pricewise.enums.AvailabilityStatus;
import com.pricewise.exception.ResourceNotFoundException;
import com.pricewise.repository.ProductOfferRepository;
import com.pricewise.repository.ProductRepository;
import com.pricewise.repository.RetailerRepository;
import com.pricewise.util.PriceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class PriceComparisonService {

    private final ProductRepository productRepository;
    private final ProductOfferRepository offerRepository;
    private final RetailerRepository retailerRepository;
    private final RetailerConfigProperties retailerConfigProperties;

    public PriceComparisonService(
            ProductRepository productRepository,
            ProductOfferRepository offerRepository,
            RetailerRepository retailerRepository,
            RetailerConfigProperties retailerConfigProperties) {
        this.productRepository = productRepository;
        this.offerRepository = offerRepository;
        this.retailerRepository = retailerRepository;
        this.retailerConfigProperties = retailerConfigProperties;
    }

    @Transactional(readOnly = true)
    public PriceComparisonResponseDTO compareProductOffers(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        List<ProductOffer> offers = offerRepository.findByProductIdAndActiveTrue(productId);
        Map<Long, Retailer> retailerMap = new HashMap<>();
        for (Retailer r : retailerRepository.findAll()) {
            retailerMap.put(r.getId(), r);
        }

        int freshnessThresholdHours = retailerConfigProperties.getRetailers().getSync().getFreshnessThresholdHours();
        OffsetDateTime staleCutoff = OffsetDateTime.now().minusHours(freshnessThresholdHours);

        List<ProductOfferResponseDTO> offerDTOs = new ArrayList<>();
        BigDecimal lowestPrice = null;
        BigDecimal highestPrice = null;
        Long lowestOfferRetailerId = null;
        String lowestOfferRetailerName = null;
        boolean hasStaleOffer = false;
        boolean allDeliveryKnown = true;
        OffsetDateTime mostRecentUpdate = null;

        for (ProductOffer offer : offers) {
            Retailer retailer = retailerMap.get(offer.getRetailerId());
            ProductOfferResponseDTO dto = new ProductOfferResponseDTO();
            dto.setId(offer.getId());
            dto.setProductId(offer.getProductId());
            dto.setRetailerId(offer.getRetailerId());
            dto.setRetailerName(retailer != null ? retailer.getName() : "Unknown Retailer");
            dto.setRetailerCode(retailer != null ? retailer.getCode() : "UNKNOWN");
            dto.setRetailerLogoUrl(retailer != null ? retailer.getLogoUrl() : null);
            dto.setExternalProductId(offer.getExternalProductId());
            dto.setProductTitle(offer.getExternalTitle());
            dto.setProductUrl(offer.getProductUrl());
            dto.setImageUrl(offer.getImageUrl());
            dto.setCurrentPrice(offer.getCurrentPrice());
            dto.setOriginalPrice(offer.getOriginalPrice());
            dto.setCurrency(offer.getCurrency());
            dto.setAvailabilityStatus(offer.getAvailabilityStatus());
            dto.setLastFetchedAt(offer.getLastFetchedAt());
            dto.setLastSuccessfulFetchAt(offer.getLastSuccessfulFetchAt());
            dto.setActive(offer.getActive());

            // Track freshness
            boolean isStale = offer.getLastSuccessfulFetchAt() == null || offer.getLastSuccessfulFetchAt().isBefore(staleCutoff);
            dto.setIsStale(isStale);
            if (isStale) {
                hasStaleOffer = true;
            }

            if (mostRecentUpdate == null || (offer.getLastSuccessfulFetchAt() != null && offer.getLastSuccessfulFetchAt().isAfter(mostRecentUpdate))) {
                mostRecentUpdate = offer.getLastSuccessfulFetchAt();
            }

            // Discount calculation: ((originalPrice - currentPrice) / originalPrice) * 100
            BigDecimal discount = PriceUtils.calculateDiscountPercentage(offer.getOriginalPrice(), offer.getCurrentPrice());
            dto.setDiscountPercentage(discount);

            // Delivery charge handling
            if (offer.getDeliveryCharge() != null) {
                dto.setDeliveryCharge(offer.getDeliveryCharge());
                dto.setIsDeliveryUnknown(false);
                dto.setTotalDeliveredPrice(PriceUtils.calculateDeliveredPrice(offer.getCurrentPrice(), offer.getDeliveryCharge()));
            } else {
                dto.setDeliveryCharge(null);
                dto.setIsDeliveryUnknown(true);
                dto.setTotalDeliveredPrice(null);
                allDeliveryKnown = false;
            }

            // Track min and max prices
            if (offer.getCurrentPrice() != null && offer.getCurrentPrice().compareTo(BigDecimal.ZERO) > 0) {
                if (lowestPrice == null || offer.getCurrentPrice().compareTo(lowestPrice) < 0) {
                    lowestPrice = offer.getCurrentPrice();
                    lowestOfferRetailerId = offer.getRetailerId();
                    lowestOfferRetailerName = retailer != null ? retailer.getName() : "Retailer";
                }
                if (highestPrice == null || offer.getCurrentPrice().compareTo(highestPrice) > 0) {
                    highestPrice = offer.getCurrentPrice();
                }
            }

            offerDTOs.add(dto);
        }

        // Mark lowest price flags
        for (ProductOfferResponseDTO dto : offerDTOs) {
            if (lowestPrice != null && dto.getCurrentPrice() != null && dto.getCurrentPrice().compareTo(lowestPrice) == 0) {
                dto.setIsLowestPrice(true);
            } else {
                dto.setIsLowestPrice(false);
            }
        }

        // Sort offers by currentPrice ascending
        offerDTOs.sort(Comparator.comparing(ProductOfferResponseDTO::getCurrentPrice, Comparator.nullsLast(BigDecimal::compareTo)));

        PriceComparisonResponseDTO response = new PriceComparisonResponseDTO();
        response.setCanonicalProductId(product.getId());
        response.setProductName(product.getName());
        response.setProductVariantInformation(product.getModelNumber() != null ? "Model: " + product.getModelNumber() : "");
        response.setAvailableOffers(offerDTOs);
        response.setLowestVerifiedComparablePrice(lowestPrice);
        response.setLowestOfferRetailerId(lowestOfferRetailerId);
        response.setLowestOfferRetailerName(lowestOfferRetailerName);
        response.setPriceCurrency("INR");
        response.setAllDeliveryCostsKnown(allDeliveryKnown);
        response.setLastSuccessfulUpdateTimestamp(mostRecentUpdate != null ? mostRecentUpdate : OffsetDateTime.now());

        if (highestPrice != null && lowestPrice != null) {
            response.setPriceSpread(highestPrice.subtract(lowestPrice));
        }

        // Data freshness status & transparent explanation
        if (hasStaleOffer) {
            response.setDataFreshnessStatus("PARTIALLY_STALE");
            response.setFreshnessExplanation("Notice: Some retailer offers have not been updated within the last " +
                    freshnessThresholdHours + " hours. Lowest price claim reflects current verified records.");
        } else {
            response.setDataFreshnessStatus("FRESH");
            response.setFreshnessExplanation("All retailer prices and availability verified within the freshness window.");
        }

        return response;
    }
}

