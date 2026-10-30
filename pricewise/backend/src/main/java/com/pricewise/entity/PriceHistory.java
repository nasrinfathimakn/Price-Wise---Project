package com.pricewise.entity;

import com.pricewise.enums.AvailabilityStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "price_history")
public class PriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_offer_id", nullable = false)
    private Long productOfferId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "delivery_charge", precision = 12, scale = 2)
    private BigDecimal deliveryCharge;

    @Column(nullable = false, length = 10)
    private String currency = "INR";

    @Enumerated(EnumType.STRING)
    @Column(name = "availability_status", nullable = false, length = 30)
    private AvailabilityStatus availabilityStatus = AvailabilityStatus.IN_STOCK;

    @Column(name = "observed_at", nullable = false)
    private OffsetDateTime observedAt;

    @Column(name = "data_source", nullable = false, length = 50)
    private String dataSource = "MOCK";

    public PriceHistory() {
    }

    public PriceHistory(Long id, Long productOfferId, BigDecimal price, BigDecimal deliveryCharge, String currency, AvailabilityStatus availabilityStatus, OffsetDateTime observedAt, String dataSource) {
        this.id = id;
        this.productOfferId = productOfferId;
        this.price = price;
        this.deliveryCharge = deliveryCharge;
        this.currency = currency != null ? currency : "INR";
        this.availabilityStatus = availabilityStatus != null ? availabilityStatus : AvailabilityStatus.IN_STOCK;
        this.observedAt = observedAt != null ? observedAt : OffsetDateTime.now();
        this.dataSource = dataSource != null ? dataSource : "MOCK";
    }

    @PrePersist
    protected void onCreate() {
        if (this.observedAt == null) {
            this.observedAt = OffsetDateTime.now();
        }
        if (this.currency == null) {
            this.currency = "INR";
        }
        if (this.availabilityStatus == null) {
            this.availabilityStatus = AvailabilityStatus.IN_STOCK;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductOfferId() {
        return productOfferId;
    }

    public void setProductOfferId(Long productOfferId) {
        this.productOfferId = productOfferId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(BigDecimal deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public OffsetDateTime getObservedAt() {
        return observedAt;
    }

    public void setObservedAt(OffsetDateTime observedAt) {
        this.observedAt = observedAt;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
}

