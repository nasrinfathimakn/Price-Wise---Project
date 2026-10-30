package com.pricewise.dto;

public class ProductSpecificationDTO {
    private Long id;
    private String attributeName;
    private String attributeValue;

    public ProductSpecificationDTO() {
    }

    public ProductSpecificationDTO(Long id, String attributeName, String attributeValue) {
        this.id = id;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }
}

