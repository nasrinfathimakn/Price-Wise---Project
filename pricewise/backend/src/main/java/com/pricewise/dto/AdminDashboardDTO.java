package com.pricewise.dto;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardDTO {
    private long totalProducts;
    private long totalOffers;
    private long activeRetailers;
    private long totalUsers;
    private long activeAlerts;
    private long pendingUncertainMatches;
    private List<PriceUpdateLogDTO> recentUpdateLogs = new ArrayList<>();

    public AdminDashboardDTO() {
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public long getTotalOffers() {
        return totalOffers;
    }

    public void setTotalOffers(long totalOffers) {
        this.totalOffers = totalOffers;
    }

    public long getActiveRetailers() {
        return activeRetailers;
    }

    public void setActiveRetailers(long activeRetailers) {
        this.activeRetailers = activeRetailers;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getActiveAlerts() {
        return activeAlerts;
    }

    public void setActiveAlerts(long activeAlerts) {
        this.activeAlerts = activeAlerts;
    }

    public long getPendingUncertainMatches() {
        return pendingUncertainMatches;
    }

    public void setPendingUncertainMatches(long pendingUncertainMatches) {
        this.pendingUncertainMatches = pendingUncertainMatches;
    }

    public List<PriceUpdateLogDTO> getRecentUpdateLogs() {
        return recentUpdateLogs;
    }

    public void setRecentUpdateLogs(List<PriceUpdateLogDTO> recentUpdateLogs) {
        this.recentUpdateLogs = recentUpdateLogs;
    }
}

