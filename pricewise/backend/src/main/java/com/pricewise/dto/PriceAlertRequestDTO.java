package com.pricewise.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PriceAlertRequestDTO {

    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @NotNull(message = "Target price cannot be null")
    @DecimalMin(value = "1.0", message = "Target price must be greater than zero")
    private BigDecimal targetPrice;

    private String currency = "INR";

    public PriceAlertRequestDTO() {
    }

    public PriceAlertRequestDTO(Long productId, BigDecimal targetPrice, String currency) {
        this.productId = productId;
        this.targetPrice = targetPrice;
        this.currency = currency;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(BigDecimal targetPrice) {
        this.targetPrice = targetPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

