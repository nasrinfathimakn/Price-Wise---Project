package com.pricewise.controller;

import com.pricewise.dto.CategoryResponseDTO;
import com.pricewise.dto.ProductSearchResponseDTO;
import com.pricewise.entity.Category;
import com.pricewise.repository.CategoryRepository;
import com.pricewise.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Categories", description = "Endpoints for exploring product categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final SearchService searchService;

    public CategoryController(CategoryRepository categoryRepository, SearchService searchService) {
        this.categoryRepository = categoryRepository;
        this.searchService = searchService;
    }

    @GetMapping
    @Operation(summary = "List all product categories")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<CategoryResponseDTO> categories = categoryRepository.findAll().stream()
                .map(c -> new CategoryResponseDTO(c.getId(), c.getName(), c.getDescription(), c.getParentCategoryId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category details by ID")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(c -> ResponseEntity.ok(new CategoryResponseDTO(c.getId(), c.getName(), c.getDescription(), c.getParentCategoryId())))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/products")
    @Operation(summary = "Retrieve products in a category with pagination")
    public ResponseEntity<ProductSearchResponseDTO> getProductsByCategory(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "relevance") String sort) {
        ProductSearchResponseDTO result = searchService.search(null, null, id, null, null, sort, page, size);
        return ResponseEntity.ok(result);
    }
}

