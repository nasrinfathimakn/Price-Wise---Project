package com.pricewise.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class PriceComparisonResponseDTO {
    private Long canonicalProductId;
    private String productName;
    private String productVariantInformation;
    private List<ProductOfferResponseDTO> availableOffers = new ArrayList<>();
    private BigDecimal lowestVerifiedComparablePrice;
    private Long lowestOfferRetailerId;
    private String lowestOfferRetailerName;
    private String priceCurrency = "INR";
    private String dataFreshnessStatus; // "FRESH", "PARTIALLY_STALE", "STALE", "MOCK_DATA"
    private OffsetDateTime lastSuccessfulUpdateTimestamp;
    private String freshnessExplanation;
    private Boolean allDeliveryCostsKnown;
    private BigDecimal priceSpread; // highest offer - lowest offer

    public PriceComparisonResponseDTO() {
    }

    // Getters and Setters
    public Long getCanonicalProductId() {
        return canonicalProductId;
    }

    public void setCanonicalProductId(Long canonicalProductId) {
        this.canonicalProductId = canonicalProductId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductVariantInformation() {
        return productVariantInformation;
    }

    public void setProductVariantInformation(String productVariantInformation) {
        this.productVariantInformation = productVariantInformation;
    }

    public List<ProductOfferResponseDTO> getAvailableOffers() {
        return availableOffers;
    }

    public void setAvailableOffers(List<ProductOfferResponseDTO> availableOffers) {
        this.availableOffers = availableOffers;
    }

    public BigDecimal getLowestVerifiedComparablePrice() {
        return lowestVerifiedComparablePrice;
    }

    public void setLowestVerifiedComparablePrice(BigDecimal lowestVerifiedComparablePrice) {
        this.lowestVerifiedComparablePrice = lowestVerifiedComparablePrice;
    }

    public Long getLowestOfferRetailerId() {
        return lowestOfferRetailerId;
    }

    public void setLowestOfferRetailerId(Long lowestOfferRetailerId) {
        this.lowestOfferRetailerId = lowestOfferRetailerId;
    }

    public String getLowestOfferRetailerName() {
        return lowestOfferRetailerName;
    }

    public void setLowestOfferRetailerName(String lowestOfferRetailerName) {
        this.lowestOfferRetailerName = lowestOfferRetailerName;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public String getDataFreshnessStatus() {
        return dataFreshnessStatus;
    }

    public void setDataFreshnessStatus(String dataFreshnessStatus) {
        this.dataFreshnessStatus = dataFreshnessStatus;
    }

    public OffsetDateTime getLastSuccessfulUpdateTimestamp() {
        return lastSuccessfulUpdateTimestamp;
    }

    public void setLastSuccessfulUpdateTimestamp(OffsetDateTime lastSuccessfulUpdateTimestamp) {
        this.lastSuccessfulUpdateTimestamp = lastSuccessfulUpdateTimestamp;
    }

    public String getFreshnessExplanation() {
        return freshnessExplanation;
    }

    public void setFreshnessExplanation(String freshnessExplanation) {
        this.freshnessExplanation = freshnessExplanation;
    }

    public Boolean getAllDeliveryCostsKnown() {
        return allDeliveryCostsKnown;
    }

    public void setAllDeliveryCostsKnown(Boolean allDeliveryCostsKnown) {
        this.allDeliveryCostsKnown = allDeliveryCostsKnown;
    }

    public BigDecimal getPriceSpread() {
        return priceSpread;
    }

    public void setPriceSpread(BigDecimal priceSpread) {
        this.priceSpread = priceSpread;
    }
}

