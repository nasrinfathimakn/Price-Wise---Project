package com.pricewise.entity;

import com.pricewise.enums.PriceUpdateStatus;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "price_update_logs")
public class PriceUpdateLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "retailer_id")
    private Long retailerId;

    @Column(name = "started_at", nullable = false)
    private OffsetDateTime startedAt;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PriceUpdateStatus status = PriceUpdateStatus.RUNNING;

    @Column(name = "offers_processed")
    private Integer offersProcessed = 0;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    public PriceUpdateLog() {
    }

    public PriceUpdateLog(Long id, Long retailerId, OffsetDateTime startedAt, PriceUpdateStatus status) {
        this.id = id;
        this.retailerId = retailerId;
        this.startedAt = startedAt != null ? startedAt : OffsetDateTime.now();
        this.status = status != null ? status : PriceUpdateStatus.RUNNING;
        this.offersProcessed = 0;
    }

    @PrePersist
    protected void onCreate() {
        if (this.startedAt == null) {
            this.startedAt = OffsetDateTime.now();
        }
        if (this.status == null) {
            this.status = PriceUpdateStatus.RUNNING;
        }
    }

    // Getters and Setters
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

