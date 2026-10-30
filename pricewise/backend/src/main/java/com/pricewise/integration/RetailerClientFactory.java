package com.pricewise.integration;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class RetailerClientFactory {

    private final Map<String, RetailerClient> clients = new HashMap<>();
    private final MockRetailerClient mockRetailerClient;

    public RetailerClientFactory(List<RetailerClient> clientList, MockRetailerClient mockRetailerClient) {
        this.mockRetailerClient = mockRetailerClient;
        for (RetailerClient client : clientList) {
            clients.put(client.getRetailerCode().toUpperCase(), client);
        }
    }

    public RetailerClient getClient(String retailerCode) {
        if (retailerCode == null) {
            return mockRetailerClient;
        }
        return Optional.ofNullable(clients.get(retailerCode.toUpperCase()))
                .orElse(mockRetailerClient);
    }
}

