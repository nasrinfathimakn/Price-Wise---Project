package com.pricewise;

import com.pricewise.dto.PriceAlertRequestDTO;
import com.pricewise.dto.PriceAlertResponseDTO;
import com.pricewise.entity.PriceAlert;
import com.pricewise.entity.Product;
import com.pricewise.entity.ProductOffer;
import com.pricewise.entity.User;
import com.pricewise.repository.PriceAlertRepository;
import com.pricewise.repository.ProductOfferRepository;
import com.pricewise.repository.ProductRepository;
import com.pricewise.repository.UserRepository;
import com.pricewise.service.EmailNotificationService;
import com.pricewise.service.PriceAlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PriceAlertServiceTest {

    @Mock
    private PriceAlertRepository alertRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductOfferRepository offerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailNotificationService emailNotificationService;

    private PriceAlertService priceAlertService;

    @BeforeEach
    void setUp() {
        priceAlertService = new PriceAlertService(
                alertRepository, productRepository, offerRepository, userRepository, emailNotificationService
        );
    }

    @Test
    @DisplayName("Creates active price alert successfully")
    void testCreateAlert() {
        User user = new User(1L, "John", "john@example.com", "pass", null, true);
        Product product = new Product(10L, "Samsung Galaxy S25", 2L, 2L, "SM-S931B", "Desc", "url", null, true);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(alertRepository.save(any(PriceAlert.class))).thenAnswer(i -> {
            PriceAlert a = i.getArgument(0);
            a.setId(99L);
            return a;
        });

        PriceAlertRequestDTO req = new PriceAlertRequestDTO(10L, new BigDecimal("70000.00"), "INR");
        PriceAlertResponseDTO res = priceAlertService.createAlert("john@example.com", req);

        assertNotNull(res);
        assertEquals(99L, res.getId());
        assertEquals(new BigDecimal("70000.00"), res.getTargetPrice());
        assertTrue(res.getActive());
    }

    @Test
    @DisplayName("Evaluates active alerts and triggers notification when target price reached")
    void testEvaluateAlertsTriggered() {
        PriceAlert alert = new PriceAlert(5L, 1L, 10L, new BigDecimal("75000.00"), "INR");
        alert.setActive(true);

        User user = new User(1L, "John", "john@example.com", "pass", null, true);
        Product product = new Product(10L, "Samsung Galaxy S25", 2L, 2L, "SM-S931B", "Desc", "url", null, true);

        ProductOffer offer = new ProductOffer();
        offer.setCurrentPrice(new BigDecimal("73999.00")); // Below target!
        offer.setProductUrl("https://example.com/buy");

        when(alertRepository.findByActiveTrue()).thenReturn(List.of(alert));
        when(offerRepository.findByProductIdAndActiveTrue(10L)).thenReturn(List.of(offer));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));

        priceAlertService.evaluateAllActiveAlerts();

        verify(emailNotificationService, times(1)).sendPriceAlertNotification(
                eq("john@example.com"),
                eq("Samsung Galaxy S25"),
                eq(new BigDecimal("75000.00")),
                eq(new BigDecimal("73999.00")),
                any(),
                any()
        );
        verify(alertRepository, times(1)).save(alert);
        assertNotNull(alert.getLastTriggeredAt());
    }

    @Test
    @DisplayName("Suppresses notification when alert was already triggered in past 24 hours")
    void testEvaluateAlertsThrottled() {
        PriceAlert alert = new PriceAlert(5L, 1L, 10L, new BigDecimal("75000.00"), "INR");
        alert.setActive(true);
        alert.setLastTriggeredAt(OffsetDateTime.now().minusHours(2)); // Triggered 2 hours ago

        ProductOffer offer = new ProductOffer();
        offer.setCurrentPrice(new BigDecimal("73999.00"));

        when(alertRepository.findByActiveTrue()).thenReturn(List.of(alert));
        when(offerRepository.findByProductIdAndActiveTrue(10L)).thenReturn(List.of(offer));

        priceAlertService.evaluateAllActiveAlerts();

        verify(emailNotificationService, never()).sendPriceAlertNotification(any(), any(), any(), any(), any(), any());
    }
}

