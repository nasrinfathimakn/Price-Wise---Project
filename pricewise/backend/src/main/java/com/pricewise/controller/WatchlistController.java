package com.pricewise.controller;

import com.pricewise.dto.WatchlistResponseDTO;
import com.pricewise.security.SecurityUtils;
import com.pricewise.service.WatchlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/watchlist")
@Tag(name = "Watchlist", description = "Endpoints for managing the user's personal product watchlist")
public class WatchlistController {

    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @GetMapping
    @Operation(summary = "Get current authenticated user's watchlist")
    public ResponseEntity<List<WatchlistResponseDTO>> getUserWatchlist() {
        String email = SecurityUtils.getCurrentUserEmail();
        return ResponseEntity.ok(watchlistService.getUserWatchlist(email));
    }

    @PostMapping
    @Operation(summary = "Add a product to watchlist")
    public ResponseEntity<WatchlistResponseDTO> addToWatchlist(@RequestBody Map<String, Long> payload) {
        String email = SecurityUtils.getCurrentUserEmail();
        Long productId = payload.get("productId");
        if (productId == null) {
            throw new IllegalArgumentException("productId is required in request body");
        }
        WatchlistResponseDTO item = watchlistService.addToWatchlist(email, productId);
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Remove a product from watchlist")
    public ResponseEntity<Map<String, String>> removeFromWatchlist(@PathVariable Long productId) {
        String email = SecurityUtils.getCurrentUserEmail();
        watchlistService.removeFromWatchlist(email, productId);
        return ResponseEntity.ok(Map.of("message", "Product removed from watchlist successfully"));
    }
}

