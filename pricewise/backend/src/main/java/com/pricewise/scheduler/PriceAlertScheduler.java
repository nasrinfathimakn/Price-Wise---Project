package com.pricewise.scheduler;

import com.pricewise.service.PriceAlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PriceAlertScheduler {

    private static final Logger log = LoggerFactory.getLogger(PriceAlertScheduler.class);
    private final PriceAlertService priceAlertService;

    public PriceAlertScheduler(PriceAlertService priceAlertService) {
        this.priceAlertService = priceAlertService;
    }

    @Scheduled(fixedRateString = "${pricewise.alerts.evaluation-rate-ms:1800000}", initialDelay = 30000)
    public void runPriceAlertEvaluations() {
        log.info("Running scheduled price alert condition evaluation...");
        try {
            priceAlertService.evaluateAllActiveAlerts();
        } catch (Exception ex) {
            log.error("Scheduled price alert evaluation encountered error: {}", ex.getMessage());
        }
    }
}

