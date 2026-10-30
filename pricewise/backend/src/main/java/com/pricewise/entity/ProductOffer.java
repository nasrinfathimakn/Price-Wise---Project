package com.pricewise.entity;

import com.pricewise.enums.AvailabilityStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "product_offers", uniqueConstraints = {
    @UniqueConstraint(name = "uq_product_retailer", columnNames = {"product_id", "retailer_id"})
})
public class ProductOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "retailer_id", nullable = false)
    private Long retailerId;

    @Column(name = "external_product_id", nullable = false, length = 100)
    private String externalProductId;

    @Column(name = "external_title", nullable = false, length = 255)
    private String externalTitle;

    @Column(name = "product_url", nullable = false, length = 1000)
    private String productUrl;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "current_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentPrice;

    @Column(name = "original_price", precision = 12, scale = 2)
    private BigDecimal originalPrice;

    @Column(nullable = false, length = 10)
    private String currency = "INR";

    @Column(name = "delivery_charge", precision = 12, scale = 2)
    private BigDecimal deliveryCharge;

    @Enumerated(EnumType.STRING)
    @Column(name = "availability_status", nullable = false, length = 30)
    private AvailabilityStatus availabilityStatus = AvailabilityStatus.IN_STOCK;

    @Column(name = "last_fetched_at", nullable = false)
    private OffsetDateTime lastFetchedAt;

    @Column(name = "last_successful_fetch_at", nullable = false)
    private OffsetDateTime lastSuccessfulFetchAt;

    @Column(nullable = false)
    private Boolean active = true;

    public ProductOffer() {
    }

    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        if (this.lastFetchedAt == null) {
            this.lastFetchedAt = now;
        }
        if (this.lastSuccessfulFetchAt == null) {
            this.lastSuccessfulFetchAt = now;
        }
        if (this.currency == null) {
            this.currency = "INR";
        }
        if (this.availabilityStatus == null) {
            this.availabilityStatus = AvailabilityStatus.IN_STOCK;
        }
        if (this.active == null) {
            this.active = true;
        }
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}

