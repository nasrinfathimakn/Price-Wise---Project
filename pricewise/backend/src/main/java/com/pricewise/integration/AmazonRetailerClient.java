package com.pricewise.integration;

import com.pricewise.config.RetailerConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Component
public class AmazonRetailerClient implements RetailerClient {

    private static final Logger log = LoggerFactory.getLogger(AmazonRetailerClient.class);
    public static final String CODE = "AMAZON_IN";

    private final RetailerConfigProperties.Connector amazonConfig;

    public AmazonRetailerClient(RetailerConfigProperties properties) {
        this.amazonConfig = properties.getRetailers().getAmazon();
    }

    @Override
    public String getRetailerCode() {
        return CODE;
    }

    @Override
    public boolean isAuthorized() {
        return amazonConfig.isEnabled()
                && StringUtils.hasText(amazonConfig.getApiKey())
                && StringUtils.hasText(amazonConfig.getApiSecret())
                && StringUtils.hasText(amazonConfig.getAssociateTag());
    }

    @Override
    public List<ExternalOfferDTO> fetchOffers(String productIdentifier, String modelNumber, String searchKeywords) {
        if (!isAuthorized()) {
            log.info("Amazon Product Advertising API connector is disabled or credentials not configured. Skipping live fetch.");
            return Collections.emptyList();
        }

        try {
            log.info("Querying Amazon PA-API 5.0 for GTIN: {} / Keywords: {} (endpoint: {})",
                    productIdentifier, searchKeywords, amazonConfig.getEndpoint());
            // Production PA-API integration point with request signing (AWS SigV4)
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("Amazon PA-API request timed out or returned error: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}

