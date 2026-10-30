package com.pricewise.service;

import com.pricewise.dto.*;
import com.pricewise.entity.Brand;
import com.pricewise.entity.Category;
import com.pricewise.entity.Product;
import com.pricewise.entity.ProductOffer;
import com.pricewise.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final ProductRepository productRepository;
    private final ProductOfferRepository offerRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductSpecificationRepository specificationRepository;

    public SearchService(
            ProductRepository productRepository,
            ProductOfferRepository offerRepository,
            BrandRepository brandRepository,
            CategoryRepository categoryRepository,
            ProductSpecificationRepository specificationRepository) {
        this.productRepository = productRepository;
        this.offerRepository = offerRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
        this.specificationRepository = specificationRepository;
    }

    @Transactional(readOnly = true)
    public ProductSearchResponseDTO search(
            String query,
            Long brandId,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String sortBy,
            int page,
            int size) {

        String cleanedQuery = StringUtils.hasText(query) ? query.trim() : null;

        // Determine sort order
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        if ("name_asc".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Direction.ASC, "name");
        } else if ("name_desc".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Direction.DESC, "name");
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productPage = productRepository.searchProducts(cleanedQuery, brandId, categoryId, pageable);

        Map<Long, String> brandNames = brandRepository.findAll().stream()
                .collect(Collectors.toMap(Brand::getId, Brand::getName));
        Map<Long, String> categoryNames = categoryRepository.findAll().stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        List<ProductResponseDTO> productDTOs = new ArrayList<>();
        for (Product product : productPage.getContent()) {
            List<ProductOffer> offers = offerRepository.findByProductIdAndActiveTrue(product.getId());
            BigDecimal lowestPrice = offers.stream()
                    .map(ProductOffer::getCurrentPrice)
                    .filter(Objects::nonNull)
                    .min(BigDecimal::compareTo)
                    .orElse(null);

            // Filter by price range if specified
            if (minPrice != null && lowestPrice != null && lowestPrice.compareTo(minPrice) < 0) {
                continue;
            }
            if (maxPrice != null && lowestPrice != null && lowestPrice.compareTo(maxPrice) > 0) {
                continue;
            }

            ProductResponseDTO dto = new ProductResponseDTO();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setBrandId(product.getBrandId());
            dto.setBrandName(brandNames.get(product.getBrandId()));
            dto.setCategoryId(product.getCategoryId());
            dto.setCategoryName(categoryNames.get(product.getCategoryId()));
            dto.setModelNumber(product.getModelNumber());
            dto.setDescription(product.getDescription());
            dto.setImageUrl(product.getImageUrl());
            dto.setProductIdentifier(product.getProductIdentifier());
            dto.setActive(product.getActive());
            dto.setLowestPrice(lowestPrice);
            dto.setOfferCount(offers.size());
            dto.setCreatedAt(product.getCreatedAt());
            dto.setUpdatedAt(product.getUpdatedAt());

            productDTOs.add(dto);
        }

        // Apply in-memory price sorting if requested
        if ("price_asc".equalsIgnoreCase(sortBy)) {
            productDTOs.sort(Comparator.comparing(ProductResponseDTO::getLowestPrice, Comparator.nullsLast(BigDecimal::compareTo)));
        } else if ("price_desc".equalsIgnoreCase(sortBy)) {
            productDTOs.sort(Comparator.comparing(ProductResponseDTO::getLowestPrice, Comparator.nullsLast(BigDecimal::compareTo)).reversed());
        }

        PageResponseDTO<ProductResponseDTO> pageResult = PageResponseDTO.of(productPage, productDTOs);

        ProductSearchResponseDTO searchResponse = new ProductSearchResponseDTO(pageResult, query);
        searchResponse.setSelectedBrandId(brandId);
        searchResponse.setSelectedCategoryId(categoryId);
        searchResponse.setMinPrice(minPrice);
        searchResponse.setMaxPrice(maxPrice);
        searchResponse.setSortBy(sortBy);

        // Populate available filter options
        searchResponse.setAvailableCategories(categoryRepository.findAll().stream()
                .map(c -> new CategoryResponseDTO(c.getId(), c.getName(), c.getDescription(), c.getParentCategoryId()))
                .collect(Collectors.toList()));
        searchResponse.setAvailableBrands(brandRepository.findAll().stream()
                .map(b -> new BrandResponseDTO(b.getId(), b.getName(), b.getDescription()))
                .collect(Collectors.toList()));

        return searchResponse;
    }

    @Transactional(readOnly = true)
    public List<String> getAutocompleteSuggestions(String query) {
        if (!StringUtils.hasText(query) || query.trim().length() < 2) {
            return Collections.emptyList();
        }
        return productRepository.findNameSuggestions(query.trim(), PageRequest.of(0, 8));
    }
}

