package com.pricewise.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class PriceHistoryResponseDTO {
    private Long productId;
    private String productName;
    private BigDecimal lowestRecordedPrice;
    private BigDecimal highestRecordedPrice;
    private BigDecimal latestRecordedPrice;
    private String currency = "INR";
    private Boolean isHistoryAvailable;
    private List<PriceHistoryPointDTO> observations = new ArrayList<>();

    public PriceHistoryResponseDTO() {
    }

    public static class PriceHistoryPointDTO {
        private Long id;
        private Long productOfferId;
        private String retailerName;
        private BigDecimal price;
        private BigDecimal deliveryCharge;
        private OffsetDateTime observedAt;
        private String formattedDate;

        public PriceHistoryPointDTO() {
        }

        public PriceHistoryPointDTO(Long id, Long productOfferId, String retailerName, BigDecimal price, BigDecimal deliveryCharge, OffsetDateTime observedAt) {
            this.id = id;
            this.productOfferId = productOfferId;
            this.retailerName = retailerName;
            this.price = price;
            this.deliveryCharge = deliveryCharge;
            this.observedAt = observedAt;
            this.formattedDate = observedAt != null ? observedAt.toLocalDate().toString() : "";
        }

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

        public String getRetailerName() {
            return retailerName;
        }

        public void setRetailerName(String retailerName) {
            this.retailerName = retailerName;
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

        public OffsetDateTime getObservedAt() {
            return observedAt;
        }

        public void setObservedAt(OffsetDateTime observedAt) {
            this.observedAt = observedAt;
            this.formattedDate = observedAt != null ? observedAt.toLocalDate().toString() : "";
        }

        public String getFormattedDate() {
            return formattedDate;
        }

        public void setFormattedDate(String formattedDate) {
            this.formattedDate = formattedDate;
        }
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getLowestRecordedPrice() {
        return lowestRecordedPrice;
    }

    public void setLowestRecordedPrice(BigDecimal lowestRecordedPrice) {
        this.lowestRecordedPrice = lowestRecordedPrice;
    }

    public BigDecimal getHighestRecordedPrice() {
        return highestRecordedPrice;
    }

    public void setHighestRecordedPrice(BigDecimal highestRecordedPrice) {
        this.highestRecordedPrice = highestRecordedPrice;
    }

    public BigDecimal getLatestRecordedPrice() {
        return latestRecordedPrice;
    }

    public void setLatestRecordedPrice(BigDecimal latestRecordedPrice) {
        this.latestRecordedPrice = latestRecordedPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getIsHistoryAvailable() {
        return isHistoryAvailable;
    }

    public void setIsHistoryAvailable(Boolean isHistoryAvailable) {
        this.isHistoryAvailable = isHistoryAvailable;
    }

    public List<PriceHistoryPointDTO> getObservations() {
        return observations;
    }

    public void setObservations(List<PriceHistoryPointDTO> observations) {
        this.observations = observations;
    }
}

