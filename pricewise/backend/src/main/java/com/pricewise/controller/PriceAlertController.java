package com.pricewise.controller;

import com.pricewise.dto.PriceAlertRequestDTO;
import com.pricewise.dto.PriceAlertResponseDTO;
import com.pricewise.security.SecurityUtils;
import com.pricewise.service.PriceAlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/price-alerts")
@Tag(name = "Price Alerts", description = "Endpoints for configuring and tracking target-price alerts")
public class PriceAlertController {

    private final PriceAlertService priceAlertService;

    public PriceAlertController(PriceAlertService priceAlertService) {
        this.priceAlertService = priceAlertService;
    }

    @GetMapping
    @Operation(summary = "List current user's price alerts")
    public ResponseEntity<List<PriceAlertResponseDTO>> getUserAlerts() {
        String email = SecurityUtils.getCurrentUserEmail();
        return ResponseEntity.ok(priceAlertService.getUserAlerts(email));
    }

    @PostMapping
    @Operation(summary = "Create a target-price alert")
    public ResponseEntity<PriceAlertResponseDTO> createAlert(@Valid @RequestBody PriceAlertRequestDTO request) {
        String email = SecurityUtils.getCurrentUserEmail();
        PriceAlertResponseDTO alert = priceAlertService.createAlert(email, request);
        return new ResponseEntity<>(alert, HttpStatus.CREATED);
    }

    @PatchMapping("/{alertId}")
    @Operation(summary = "Toggle or update active state of a price alert")
    public ResponseEntity<PriceAlertResponseDTO> updateAlert(
            @PathVariable Long alertId,
            @RequestBody(required = false) Map<String, Boolean> payload) {
        String email = SecurityUtils.getCurrentUserEmail();
        Boolean active = payload != null ? payload.get("active") : null;
        PriceAlertResponseDTO updated = priceAlertService.toggleAlertStatus(email, alertId, active);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{alertId}")
    @Operation(summary = "Delete a price alert")
    public ResponseEntity<Map<String, String>> deleteAlert(@PathVariable Long alertId) {
        String email = SecurityUtils.getCurrentUserEmail();
        priceAlertService.deleteAlert(email, alertId);
        return ResponseEntity.ok(Map.of("message", "Price alert deleted successfully"));
    }
}

