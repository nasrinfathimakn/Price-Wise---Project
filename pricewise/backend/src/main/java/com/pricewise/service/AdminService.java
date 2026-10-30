package com.pricewise.service;

import com.pricewise.dto.*;
import com.pricewise.entity.PriceUpdateLog;
import com.pricewise.entity.Retailer;
import com.pricewise.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final ProductRepository productRepository;
    private final ProductOfferRepository offerRepository;
    private final RetailerRepository retailerRepository;
    private final UserRepository userRepository;
    private final PriceAlertRepository alertRepository;
    private final PriceUpdateLogRepository updateLogRepository;
    private final RetailerSyncService syncService;
    private final ProductMatchingService matchingService;

    public AdminService(
            ProductRepository productRepository,
            ProductOfferRepository offerRepository,
            RetailerRepository retailerRepository,
            UserRepository userRepository,
            PriceAlertRepository alertRepository,
            PriceUpdateLogRepository updateLogRepository,
            RetailerSyncService syncService,
            ProductMatchingService matchingService) {
        this.productRepository = productRepository;
        this.offerRepository = offerRepository;
        this.retailerRepository = retailerRepository;
        this.userRepository = userRepository;
        this.alertRepository = alertRepository;
        this.updateLogRepository = updateLogRepository;
        this.syncService = syncService;
        this.matchingService = matchingService;
    }

    @Transactional(readOnly = true)
    public AdminDashboardDTO getDashboardOverview() {
        AdminDashboardDTO dashboard = new AdminDashboardDTO();
        dashboard.setTotalProducts(productRepository.count());
        dashboard.setTotalOffers(offerRepository.count());
        dashboard.setActiveRetailers(retailerRepository.findByActiveTrue().size());
        dashboard.setTotalUsers(userRepository.count());
        dashboard.setActiveAlerts(alertRepository.findByActiveTrue().size());

        List<ProductMatchReviewDTO> pendingReviews = matchingService.getPendingReviews();
        dashboard.setPendingUncertainMatches(pendingReviews.size());

        List<PriceUpdateLogDTO> logs = updateLogRepository.findAllByOrderByStartedAtDesc().stream()
                .limit(10)
                .map(this::mapLogToDTO)
                .collect(Collectors.toList());
        dashboard.setRecentUpdateLogs(logs);

        return dashboard;
    }

    @Transactional(readOnly = true)
    public List<PriceUpdateLogDTO> getUpdateLogs() {
        return updateLogRepository.findAllByOrderByStartedAtDesc().stream()
                .map(this::mapLogToDTO)
                .collect(Collectors.toList());
    }

    public PriceUpdateLog triggerRetailerSync(Long retailerId) {
        return syncService.syncRetailer(retailerId);
    }

    public List<ProductMatchReviewDTO> getPendingMatches() {
        return matchingService.getPendingReviews();
    }

    public boolean resolveMatch(Long matchId, boolean approve) {
        return matchingService.resolveMatch(matchId, approve);
    }

    private PriceUpdateLogDTO mapLogToDTO(PriceUpdateLog log) {
        String retailerName = "All Retailers";
        if (log.getRetailerId() != null) {
            retailerName = retailerRepository.findById(log.getRetailerId())
                    .map(Retailer::getName)
                    .orElse("Retailer #" + log.getRetailerId());
        }
        return new PriceUpdateLogDTO(
                log.getId(),
                log.getRetailerId(),
                retailerName,
                log.getStartedAt(),
                log.getCompletedAt(),
                log.getStatus(),
                log.getOffersProcessed(),
                log.getErrorMessage()
        );
    }
}

