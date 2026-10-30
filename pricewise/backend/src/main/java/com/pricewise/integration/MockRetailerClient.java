package com.pricewise.integration;

import com.pricewise.enums.AvailabilityStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class MockRetailerClient implements RetailerClient {

    public static final String CODE = "MOCK";

    @Override
    public String getRetailerCode() {
        return CODE;
    }

    @Override
    public boolean isAuthorized() {
        return true;
    }

    @Override
    public List<ExternalOfferDTO> fetchOffers(String productIdentifier, String modelNumber, String searchKeywords) {
        List<ExternalOfferDTO> offers = new ArrayList<>();

        if (productIdentifier != null && productIdentifier.contains("8806095000012")) {
            // Samsung S25 256GB
            ExternalOfferDTO off1 = new ExternalOfferDTO();
            off1.setRetailerCode("AMAZON_IN");
            off1.setExternalProductId("AMZ-MOCK-S25-1");
            off1.setExternalTitle("Samsung Galaxy S25 5G (Onyx Black, 256 GB) (12 GB RAM)");
            off1.setProductUrl("https://www.amazon.in/dp/mock-s25");
            off1.setImageUrl("https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600&q=80");
            off1.setPrice(new BigDecimal("74499.00"));
            off1.setOriginalPrice(new BigDecimal("79999.00"));
            off1.setCurrency("INR");
            off1.setDeliveryCharge(BigDecimal.ZERO);
            off1.setAvailabilityStatus(AvailabilityStatus.IN_STOCK);
            off1.setGtin(productIdentifier);
            off1.setRawBrand("Samsung");
            off1.setRawModel("SM-S931B");
            offers.add(off1);
        }

        return offers;
    }
}

