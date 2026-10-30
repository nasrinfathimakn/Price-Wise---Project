package com.pricewise.dto;

import java.math.BigDecimal;

public class ProductMatchReviewDTO {
    private Long offerId;
    private Long proposedProductId;
    private String proposedProductName;
    private String retailerName;
    private String externalProductId;
    private String externalTitle;
    private BigDecimal currentPrice;
    private String matchReason;
    private Double confidenceScore;
    private String resolutionStatus; // PENDING, APPROVED, REJECTED

    public ProductMatchReviewDTO() {
    }

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public Long getProposedProductId() {
        return proposedProductId;
    }

    public void setProposedProductId(Long proposedProductId) {
        this.proposedProductId = proposedProductId;
    }

    public String getProposedProductName() {
        return proposedProductName;
    }

    public void setProposedProductName(String proposedProductName) {
        this.proposedProductName = proposedProductName;
    }

    public String getRetailerName() {
        return retailerName;
    }

    public void setRetailerName(String retailerName) {
        this.retailerName = retailerName;
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

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getMatchReason() {
        return matchReason;
    }

    public void setMatchReason(String matchReason) {
        this.matchReason = matchReason;
    }

    public Double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(Double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public String getResolutionStatus() {
        return resolutionStatus;
    }

    public void setResolutionStatus(String resolutionStatus) {
        this.resolutionStatus = resolutionStatus;
    }
}

