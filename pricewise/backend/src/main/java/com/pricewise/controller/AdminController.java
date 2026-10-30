package com.pricewise.controller;

import com.pricewise.dto.*;
import com.pricewise.entity.PriceUpdateLog;
import com.pricewise.entity.Retailer;
import com.pricewise.repository.RetailerRepository;
import com.pricewise.service.AdminService;
import com.pricewise.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin Operations", description = "Protected administrative controls for platform management")
public class AdminController {

    private final AdminService adminService;
    private final ProductService productService;
    private final RetailerRepository retailerRepository;

    public AdminController(AdminService adminService, ProductService productService, RetailerRepository retailerRepository) {
        this.adminService = adminService;
        this.productService = productService;
        this.retailerRepository = retailerRepository;
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Get admin dashboard platform counts and recent update logs")
    public ResponseEntity<AdminDashboardDTO> getDashboard() {
        return ResponseEntity.ok(adminService.getDashboardOverview());
    }

    @PostMapping("/products")
    @Operation(summary = "Create a new canonical product")
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductCreateUpdateRequestDTO request) {
        ProductResponseDTO created = productService.createProduct(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PatchMapping("/products/{id}")
    @Operation(summary = "Update an existing canonical product")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductCreateUpdateRequestDTO request) {
        ProductResponseDTO updated = productService.updateProduct(id, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/retailers")
    @Operation(summary = "List all supported retailers and their status")
    public ResponseEntity<List<RetailerResponseDTO>> getRetailers() {
        List<RetailerResponseDTO> retailers = retailerRepository.findAll().stream()
                .map(r -> new RetailerResponseDTO(r.getId(), r.getName(), r.getCode(), r.getWebsiteUrl(), r.getLogoUrl(), r.getActive(), r.getIntegrationType()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(retailers);
    }

    @PostMapping("/retailers/{id}/sync")
    @Operation(summary = "Trigger an authorized manual price sync for a retailer")
    public ResponseEntity<PriceUpdateLog> triggerSync(@PathVariable Long id) {
        PriceUpdateLog log = adminService.triggerRetailerSync(id);
        return ResponseEntity.ok(log);
    }

    @GetMapping("/update-logs")
    @Operation(summary = "Inspect history of retailer update operations and errors")
    public ResponseEntity<List<PriceUpdateLogDTO>> getUpdateLogs() {
        return ResponseEntity.ok(adminService.getUpdateLogs());
    }

    @GetMapping("/product-matches/review")
    @Operation(summary = "Review uncertain product matches flagged by the matching engine")
    public ResponseEntity<List<ProductMatchReviewDTO>> getMatchesForReview() {
        return ResponseEntity.ok(adminService.getPendingMatches());
    }

    @PostMapping("/product-matches/{id}/resolve")
    @Operation(summary = "Approve or reject a flagged product match")
    public ResponseEntity<Map<String, Object>> resolveMatch(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> payload) {
        boolean approve = payload != null && Boolean.TRUE.equals(payload.get("approve"));
        boolean success = adminService.resolveMatch(id, approve);
        return ResponseEntity.ok(Map.of(
                "success", success,
                "message", approve ? "Match approved and linked" : "Match rejected and discarded"
        ));
    }
}

