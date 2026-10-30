package com.pricewise.dto;

import com.pricewise.enums.AvailabilityStatus;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class ProductOfferResponseDTO {
    private Long id;
    private Long productId;
    private Long retailerId;
    private String retailerName;
    private String retailerCode;
    private String retailerLogoUrl;
    private String externalProductId;
    private String productTitle;
    private String productUrl;
    private String imageUrl;
    private BigDecimal currentPrice;
    private BigDecimal originalPrice;
    private BigDecimal discountPercentage;
    private String currency;
    private BigDecimal deliveryCharge;
    private Boolean isDeliveryUnknown;
    private BigDecimal totalDeliveredPrice;
    private AvailabilityStatus availabilityStatus;
    private OffsetDateTime lastFetchedAt;
    private OffsetDateTime lastSuccessfulFetchAt;
    private Boolean isLowestPrice;
    private Boolean isStale;
    private Boolean active;

    public ProductOfferResponseDTO() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(Long retailerId) {
        this.retailerId = retailerId;
    }

    public String getRetailerName() {
        return retailerName;
    }

    public void setRetailerName(String retailerName) {
        this.retailerName = retailerName;
    }

    public String getRetailerCode() {
        return retailerCode;
    }

    public void setRetailerCode(String retailerCode) {
        this.retailerCode = retailerCode;
    }

    public String getRetailerLogoUrl() {
        return retailerLogoUrl;
    }

    public void setRetailerLogoUrl(String retailerLogoUrl) {
        this.retailerLogoUrl = retailerLogoUrl;
    }

    public String getExternalProductId() {
        return externalProductId;
    }

    public void setExternalProductId(String externalProductId) {
        this.externalProductId = externalProductId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(BigDecimal deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public Boolean getIsDeliveryUnknown() {
        return isDeliveryUnknown;
    }

    public void setIsDeliveryUnknown(Boolean isDeliveryUnknown) {
        this.isDeliveryUnknown = isDeliveryUnknown;
    }

    public BigDecimal getTotalDeliveredPrice() {
        return totalDeliveredPrice;
    }

    public void setTotalDeliveredPrice(BigDecimal totalDeliveredPrice) {
        this.totalDeliveredPrice = totalDeliveredPrice;
    }

    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public OffsetDateTime getLastFetchedAt() {
        return lastFetchedAt;
    }

    public void setLastFetchedAt(OffsetDateTime lastFetchedAt) {
        this.lastFetchedAt = lastFetchedAt;
    }

    public OffsetDateTime getLastSuccessfulFetchAt() {
        return lastSuccessfulFetchAt;
    }

    public void setLastSuccessfulFetchAt(OffsetDateTime lastSuccessfulFetchAt) {
        this.lastSuccessfulFetchAt = lastSuccessfulFetchAt;
    }

    public Boolean getIsLowestPrice() {
        return isLowestPrice;
    }

    public void setIsLowestPrice(Boolean isLowestPrice) {
        this.isLowestPrice = isLowestPrice;
    }

    public Boolean getIsStale() {
        return isStale;
    }

    public void setIsStale(Boolean isStale) {
        this.isStale = isStale;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}

