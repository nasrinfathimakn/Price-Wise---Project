package com.pricewise.dto;

import java.math.BigDecimal;
import java.util.List;

public class ProductSearchResponseDTO {
    private PageResponseDTO<ProductResponseDTO> results;
    private String query;
    private Long selectedCategoryId;
    private Long selectedBrandId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String sortBy;
    private List<CategoryResponseDTO> availableCategories;
    private List<BrandResponseDTO> availableBrands;

    public ProductSearchResponseDTO() {
    }

    public ProductSearchResponseDTO(PageResponseDTO<ProductResponseDTO> results, String query) {
        this.results = results;
        this.query = query;
    }

    public PageResponseDTO<ProductResponseDTO> getResults() {
        return results;
    }

    public void setResults(PageResponseDTO<ProductResponseDTO> results) {
        this.results = results;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Long getSelectedCategoryId() {
        return selectedCategoryId;
    }

    public void setSelectedCategoryId(Long selectedCategoryId) {
        this.selectedCategoryId = selectedCategoryId;
    }

    public Long getSelectedBrandId() {
        return selectedBrandId;
    }

    public void setSelectedBrandId(Long selectedBrandId) {
        this.selectedBrandId = selectedBrandId;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public List<CategoryResponseDTO> getAvailableCategories() {
        return availableCategories;
    }

    public void setAvailableCategories(List<CategoryResponseDTO> availableCategories) {
        this.availableCategories = availableCategories;
    }

    public List<BrandResponseDTO> getAvailableBrands() {
        return availableBrands;
    }

    public void setAvailableBrands(List<BrandResponseDTO> availableBrands) {
        this.availableBrands = availableBrands;
    }
}

