package com.pricewise.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class ProductCreateUpdateRequestDTO {

    @NotBlank(message = "Product name is required")
    private String name;

    private Long brandId;
    private Long categoryId;
    private String modelNumber;
    private String description;
    private String imageUrl;
    private String productIdentifier;
    private Boolean active = true;
    private List<ProductSpecificationDTO> specifications;

    public ProductCreateUpdateRequestDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductIdentifier() {
        return productIdentifier;
    }

    public void setProductIdentifier(String productIdentifier) {
        this.productIdentifier = productIdentifier;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<ProductSpecificationDTO> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<ProductSpecificationDTO> specifications) {
        this.specifications = specifications;
    }
}

