package com.pricewise.dto;

import com.pricewise.enums.PriceUpdateStatus;
import java.time.OffsetDateTime;

public class PriceUpdateLogDTO {
    private Long id;
    private Long retailerId;
    private String retailerName;
    private OffsetDateTime startedAt;
    private OffsetDateTime completedAt;
    private PriceUpdateStatus status;
    private Integer offersProcessed;
    private String errorMessage;

    public PriceUpdateLogDTO() {
    }

    public PriceUpdateLogDTO(Long id, Long retailerId, String retailerName, OffsetDateTime startedAt, OffsetDateTime completedAt, PriceUpdateStatus status, Integer offersProcessed, String errorMessage) {
        this.id = id;
        this.retailerId = retailerId;
        this.retailerName = retailerName;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.status = status;
        this.offersProcessed = offersProcessed;
        this.errorMessage = errorMessage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(Long retailerId) {
        this.retailerId = retailerId;
    }

    public String getRetailerName() {
        return retailerName;
    }

    public void setRetailerName(String retailerName) {
        this.retailerName = retailerName;
    }

    public OffsetDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(OffsetDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public OffsetDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(OffsetDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public PriceUpdateStatus getStatus() {
        return status;
    }

    public void setStatus(PriceUpdateStatus status) {
        this.status = status;
    }

    public Integer getOffersProcessed() {
        return offersProcessed;
    }

    public void setOffersProcessed(Integer offersProcessed) {
        this.offersProcessed = offersProcessed;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

