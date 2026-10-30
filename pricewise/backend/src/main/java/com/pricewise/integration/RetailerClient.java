package com.pricewise.integration;

import java.util.List;

public interface RetailerClient {

    /**
     * Unique retailer code such as AMAZON_IN, FLIPKART, CROMA.
     */
    String getRetailerCode();

    /**
     * Checks if this connector has valid credentials configured.
     */
    boolean isAuthorized();

    /**
     * Retrieves offers for a given product from the retailer.
     */
    List<ExternalOfferDTO> fetchOffers(String productIdentifier, String modelNumber, String searchKeywords);
}

