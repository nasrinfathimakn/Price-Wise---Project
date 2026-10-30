package com.pricewise.service;

import com.pricewise.dto.PriceAlertRequestDTO;
import com.pricewise.dto.PriceAlertResponseDTO;
import com.pricewise.entity.PriceAlert;
import com.pricewise.entity.Product;
import com.pricewise.entity.ProductOffer;
import com.pricewise.entity.User;
import com.pricewise.exception.ResourceNotFoundException;
import com.pricewise.repository.PriceAlertRepository;
import com.pricewise.repository.ProductOfferRepository;
import com.pricewise.repository.ProductRepository;
import com.pricewise.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class PriceAlertService {

    private static final Logger log = LoggerFactory.getLogger(PriceAlertService.class);

    private final PriceAlertRepository alertRepository;
    private final ProductRepository productRepository;
    private final ProductOfferRepository offerRepository;
    private final UserRepository userRepository;
    private final EmailNotificationService emailNotificationService;

    public PriceAlertService(
            PriceAlertRepository alertRepository,
            ProductRepository productRepository,
            ProductOfferRepository offerRepository,
            UserRepository userRepository,
            EmailNotificationService emailNotificationService) {
        this.alertRepository = alertRepository;
        this.productRepository = productRepository;
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.emailNotificationService = emailNotificationService;
    }

    @Transactional
    public PriceAlertResponseDTO createAlert(String userEmail, PriceAlertRequestDTO request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        PriceAlert alert = new PriceAlert();
        alert.setUserId(user.getId());
        alert.setProductId(product.getId());
        alert.setTargetPrice(request.getTargetPrice());
        alert.setCurrency(request.getCurrency() != null ? request.getCurrency() : "INR");
        alert.setActive(true);

        PriceAlert saved = alertRepository.save(alert);
        return mapToDTO(saved, product);
    }

    @Transactional(readOnly = true)
    public List<PriceAlertResponseDTO> getUserAlerts(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        List<PriceAlert> alerts = alertRepository.findByUserId(user.getId());
        List<PriceAlertResponseDTO> dtos = new ArrayList<>();

        for (PriceAlert alert : alerts) {
            productRepository.findById(alert.getProductId())
                    .ifPresent(product -> dtos.add(mapToDTO(alert, product)));
        }

        return dtos;
    }

    @Transactional
    public PriceAlertResponseDTO toggleAlertStatus(String userEmail, Long alertId, Boolean active) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        PriceAlert alert = alertRepository.findByIdAndUserId(alertId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Price alert not found with id: " + alertId));

        if (active != null) {
            alert.setActive(active);
        } else {
            alert.setActive(!alert.getActive());
        }

        PriceAlert updated = alertRepository.save(alert);
        Product product = productRepository.findById(updated.getProductId()).orElse(null);
        return mapToDTO(updated, product);
    }

    @Transactional
    public void deleteAlert(String userEmail, Long alertId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        PriceAlert alert = alertRepository.findByIdAndUserId(alertId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Price alert not found with id: " + alertId));

        alertRepository.delete(alert);
    }

    @Transactional
    public void evaluateAllActiveAlerts() {
        List<PriceAlert> activeAlerts = alertRepository.findByActiveTrue();
        log.info("Evaluating {} active price alert rules...", activeAlerts.size());

        for (PriceAlert alert : activeAlerts) {
            List<ProductOffer> offers = offerRepository.findByProductIdAndActiveTrue(alert.getProductId());
            Optional<ProductOffer> lowestOffer = offers.stream()
                    .filter(o -> o.getCurrentPrice() != null && o.getCurrentPrice().compareTo(BigDecimal.ZERO) > 0)
                    .min(Comparator.comparing(ProductOffer::getCurrentPrice));

            if (lowestOffer.isPresent()) {
                BigDecimal currentLowest = lowestOffer.get().getCurrentPrice();
                if (currentLowest.compareTo(alert.getTargetPrice()) <= 0) {
                    // Check if already notified within 24 hours to prevent repeated notifications
                    boolean alreadyTriggeredRecently = alert.getLastTriggeredAt() != null &&
                            alert.getLastTriggeredAt().isAfter(OffsetDateTime.now().minusHours(24));

                    if (!alreadyTriggeredRecently) {
                        alert.setLastTriggeredAt(OffsetDateTime.now());
                        alertRepository.save(alert);

                        userRepository.findById(alert.getUserId()).ifPresent(u -> {
                            productRepository.findById(alert.getProductId()).ifPresent(p -> {
                                emailNotificationService.sendPriceAlertNotification(
                                        u.getEmail(),
                                        p.getName(),
                                        alert.getTargetPrice(),
                                        currentLowest,
                                        "Retailer Offer",
                                        lowestOffer.get().getProductUrl()
                                );
                            });
                        });
                    }
                }
            }
        }
    }

    private PriceAlertResponseDTO mapToDTO(PriceAlert alert, Product product) {
        PriceAlertResponseDTO dto = new PriceAlertResponseDTO();
        dto.setId(alert.getId());
        dto.setUserId(alert.getUserId());
        dto.setProductId(alert.getProductId());
        dto.setTargetPrice(alert.getTargetPrice());
        dto.setCurrency(alert.getCurrency());
        dto.setActive(alert.getActive());
        dto.setLastTriggeredAt(alert.getLastTriggeredAt());
        dto.setCreatedAt(alert.getCreatedAt());

        if (product != null) {
            dto.setProductName(product.getName());
            dto.setProductImageUrl(product.getImageUrl());

            BigDecimal lowest = offerRepository.findLowestPriceByProductId(product.getId()).orElse(null);
            dto.setCurrentPrice(lowest);

            if (lowest != null && lowest.compareTo(alert.getTargetPrice()) <= 0) {
                dto.setIsTargetReached(true);
            } else {
                dto.setIsTargetReached(false);
            }
        }

        return dto;
    }
}

