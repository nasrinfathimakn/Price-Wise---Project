package com.pricewise.dto;

import java.math.BigDecimal;

public class ProductVariantDTO {
    private Long id;
    private String name;
    private String modelNumber;
    private BigDecimal lowestPrice;

    public ProductVariantDTO() {
    }

    public ProductVariantDTO(Long id, String name, String modelNumber, BigDecimal lowestPrice) {
        this.id = id;
        this.name = name;
        this.modelNumber = modelNumber;
        this.lowestPrice = lowestPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public BigDecimal getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(BigDecimal lowestPrice) {
        this.lowestPrice = lowestPrice;
    }
}

