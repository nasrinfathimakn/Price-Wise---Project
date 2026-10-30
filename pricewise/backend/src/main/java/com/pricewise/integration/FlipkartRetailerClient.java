package com.pricewise.integration;

import com.pricewise.config.RetailerConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Component
public class FlipkartRetailerClient implements RetailerClient {

    private static final Logger log = LoggerFactory.getLogger(FlipkartRetailerClient.class);
    public static final String CODE = "FLIPKART";

    private final RetailerConfigProperties.Connector flipkartConfig;

    public FlipkartRetailerClient(RetailerConfigProperties properties) {
        this.flipkartConfig = properties.getRetailers().getFlipkart();
    }

    @Override
    public String getRetailerCode() {
        return CODE;
    }

    @Override
    public boolean isAuthorized() {
        return flipkartConfig.isEnabled()
                && StringUtils.hasText(flipkartConfig.getAffiliateId())
                && StringUtils.hasText(flipkartConfig.getAffiliateToken());
    }

    @Override
    public List<ExternalOfferDTO> fetchOffers(String productIdentifier, String modelNumber, String searchKeywords) {
        if (!isAuthorized()) {
            log.info("Flipkart Affiliate API connector is disabled or credentials not configured. Skipping live fetch.");
            return Collections.emptyList();
        }

        try {
            log.info("Querying Flipkart Affiliate Feed for model: {} / keywords: {} (endpoint: {})",
                    modelNumber, searchKeywords, flipkartConfig.getEndpoint());
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("Flipkart API request error: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}

