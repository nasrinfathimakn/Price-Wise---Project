package com.pricewise.integration;

import com.pricewise.enums.AvailabilityStatus;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ExternalOfferDTO {
    private String retailerCode;
    private String externalProductId;
    private String externalTitle;
    private String productUrl;
    private String imageUrl;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String currency = "INR";
    private BigDecimal deliveryCharge;
    private AvailabilityStatus availabilityStatus = AvailabilityStatus.IN_STOCK;
    private String rawBrand;
    private String rawModel;
    private String gtin;
    private Map<String, String> rawAttributes = new HashMap<>();

    public ExternalOfferDTO() {
    }

    public String getRetailerCode() {
        return retailerCode;
    }

    public void setRetailerCode(String retailerCode) {
        this.retailerCode = retailerCode;
    }

    public String getExternalProductId() {
        return externalProductId;
    }

    public void setExternalProductId(String externalProductId) {
        this.externalProductId = externalProductId;
    }

    public String getExternalTitle() {
        return externalTitle;
    }

    public void setExternalTitle(String externalTitle) {
        this.externalTitle = externalTitle;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
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

    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public String getRawBrand() {
        return rawBrand;
    }

    public void setRawBrand(String rawBrand) {
        this.rawBrand = rawBrand;
    }

    public String getRawModel() {
        return rawModel;
    }

    public void setRawModel(String rawModel) {
        this.rawModel = rawModel;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public Map<String, String> getRawAttributes() {
        return rawAttributes;
    }

    public void setRawAttributes(Map<String, String> rawAttributes) {
        this.rawAttributes = rawAttributes;
    }
}

