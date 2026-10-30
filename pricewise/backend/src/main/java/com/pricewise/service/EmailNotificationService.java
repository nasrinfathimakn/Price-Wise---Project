package com.pricewise.service;

import com.pricewise.config.RetailerConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

public interface EmailNotificationService {
    boolean isConfigured();
    boolean sendPriceAlertNotification(String recipientEmail, String productName, BigDecimal targetPrice, BigDecimal currentLowestPrice, String retailerName, String dealUrl);
}

