package com.pricewise.controller;

import com.pricewise.dto.PriceComparisonResponseDTO;
import com.pricewise.dto.ProductOfferResponseDTO;
import com.pricewise.service.PriceComparisonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products/{id}")
@Tag(name = "Price Comparison", description = "Endpoints for comparing product offers across retailers")
public class PriceComparisonController {

    private final PriceComparisonService priceComparisonService;

    public PriceComparisonController(PriceComparisonService priceComparisonService) {
        this.priceComparisonService = priceComparisonService;
    }

    @GetMapping("/comparison")
    @Operation(summary = "Compare product offers across supported retailers with lowest price logic")
    public ResponseEntity<PriceComparisonResponseDTO> compareOffers(@PathVariable Long id) {
        return ResponseEntity.ok(priceComparisonService.compareProductOffers(id));
    }

    @GetMapping("/offers")
    @Operation(summary = "Retrieve raw list of retailer offers for a product")
    public ResponseEntity<List<ProductOfferResponseDTO>> getOffers(@PathVariable Long id) {
        PriceComparisonResponseDTO comparison = priceComparisonService.compareProductOffers(id);
        return ResponseEntity.ok(comparison.getAvailableOffers());
    }
}

