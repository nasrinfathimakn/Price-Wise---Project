package com.pricewise.service.impl;

import com.pricewise.config.RetailerConfigProperties;
import com.pricewise.service.EmailNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationServiceImpl.class);
    private final RetailerConfigProperties.Mail mailConfig;

    public EmailNotificationServiceImpl(RetailerConfigProperties properties) {
        this.mailConfig = properties.getMail();
    }

    @Override
    public boolean isConfigured() {
        return mailConfig.isConfigured();
    }

    @Override
    public boolean sendPriceAlertNotification(String recipientEmail, String productName, BigDecimal targetPrice, BigDecimal currentLowestPrice, String retailerName, String dealUrl) {
        if (!isConfigured()) {
            log.info("Email provider is not configured. Notification not dispatched (alerts preserved in system). Recipient: {}, Product: {}",
                    recipientEmail, productName);
            return false;
        }

        try {
            log.info("Sending price drop notification to {} for product '{}'. Target: ₹{}, Current Lowest: ₹{} ({})",
                    recipientEmail, productName, targetPrice, currentLowestPrice, retailerName);
            // Provider integration point (e.g. SES, SendGrid, SMTP)
            return true;
        } catch (Exception ex) {
            log.error("Failed to deliver price alert email to {}: {}", recipientEmail, ex.getMessage());
            return false;
        }
    }
}

