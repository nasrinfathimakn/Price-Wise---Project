package com.pricewise.controller;

import com.pricewise.dto.*;
import com.pricewise.service.ProductService;
import com.pricewise.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Products & Search", description = "Endpoints for searching, browsing, and inspecting products")
public class ProductController {

    private final ProductService productService;
    private final SearchService searchService;

    public ProductController(ProductService productService, SearchService searchService) {
        this.productService = productService;
        this.searchService = searchService;
    }

    @GetMapping("/products")
    @Operation(summary = "Search and filter products with pagination")
    public ResponseEntity<ProductSearchResponseDTO> searchProducts(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long brand,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false, defaultValue = "relevance") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        ProductSearchResponseDTO result = searchService.search(q, brand, category, minPrice, maxPrice, sort, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/products/{id}")
    @Operation(summary = "Retrieve full canonical product details")
    public ResponseEntity<ProductResponseDTO> getProductDetails(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductDetails(id));
    }

    @GetMapping("/products/{id}/specifications")
    @Operation(summary = "Retrieve product technical specifications")
    public ResponseEntity<List<ProductSpecificationDTO>> getProductSpecifications(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductSpecifications(id));
    }

    @GetMapping("/products/{id}/related")
    @Operation(summary = "Retrieve related product alternatives")
    public ResponseEntity<List<ProductResponseDTO>> getRelatedProducts(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getRelatedProducts(id));
    }

    @GetMapping("/search/suggestions")
    @Operation(summary = "Get autocomplete suggestions as user types")
    public ResponseEntity<List<String>> getSearchSuggestions(@RequestParam String q) {
        return ResponseEntity.ok(searchService.getAutocompleteSuggestions(q));
    }
}

