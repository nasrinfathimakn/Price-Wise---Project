package com.pricewise.integration;

import com.pricewise.entity.ProductOffer;
import com.pricewise.enums.AvailabilityStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
public class OfferNormalizationService {

    public ProductOffer normalizeToOffer(ExternalOfferDTO externalOffer, Long canonicalProductId, Long retailerId) {
        if (externalOffer == null || externalOffer.getPrice() == null) {
            return null;
        }

        // Validate price
        if (externalOffer.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }

        ProductOffer offer = new ProductOffer();
        offer.setProductId(canonicalProductId);
        offer.setRetailerId(retailerId);
        offer.setExternalProductId(externalOffer.getExternalProductId() != null ? externalOffer.getExternalProductId() : "EXT-" + System.currentTimeMillis());
        offer.setExternalTitle(externalOffer.getExternalTitle() != null ? externalOffer.getExternalTitle().trim() : "Unknown Title");
        offer.setProductUrl(externalOffer.getProductUrl() != null ? externalOffer.getProductUrl() : "");
        offer.setImageUrl(externalOffer.getImageUrl());
        offer.setCurrentPrice(externalOffer.getPrice());
        offer.setOriginalPrice(externalOffer.getOriginalPrice());
        offer.setCurrency(externalOffer.getCurrency() != null ? externalOffer.getCurrency().toUpperCase() : "INR");
        offer.setDeliveryCharge(externalOffer.getDeliveryCharge());
        offer.setAvailabilityStatus(externalOffer.getAvailabilityStatus() != null ? externalOffer.getAvailabilityStatus() : AvailabilityStatus.IN_STOCK);
        offer.setActive(true);
        offer.setLastFetchedAt(OffsetDateTime.now());
        offer.setLastSuccessfulFetchAt(OffsetDateTime.now());

        return offer;
    }
}

