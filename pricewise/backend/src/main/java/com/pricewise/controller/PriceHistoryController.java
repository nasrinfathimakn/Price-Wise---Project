package com.pricewise.controller;

import com.pricewise.dto.PriceHistoryResponseDTO;
import com.pricewise.service.PriceHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products/{id}")
@Tag(name = "Price History", description = "Endpoints for retrieving historical price observations")
public class PriceHistoryController {

    private final PriceHistoryService priceHistoryService;

    public PriceHistoryController(PriceHistoryService priceHistoryService) {
        this.priceHistoryService = priceHistoryService;
    }

    @GetMapping("/price-history")
    @Operation(summary = "Retrieve price history records and min/max/latest stats for charts")
    public ResponseEntity<PriceHistoryResponseDTO> getPriceHistory(@PathVariable Long id) {
        return ResponseEntity.ok(priceHistoryService.getProductPriceHistory(id));
    }
}

