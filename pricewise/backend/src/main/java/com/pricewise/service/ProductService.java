package com.pricewise.service;

import com.pricewise.dto.*;
import com.pricewise.entity.*;
import com.pricewise.exception.ResourceNotFoundException;
import com.pricewise.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductOfferRepository offerRepository;
    private final ProductSpecificationRepository specificationRepository;

    public ProductService(
            ProductRepository productRepository,
            BrandRepository brandRepository,
            CategoryRepository categoryRepository,
            ProductOfferRepository offerRepository,
            ProductSpecificationRepository specificationRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
        this.offerRepository = offerRepository;
        this.specificationRepository = specificationRepository;
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO getProductDetails(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        return buildProductResponse(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getRelatedProducts(Long productId) {
        Product currentProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        List<Product> related = productRepository.findByCategoryIdAndIdNotAndActiveTrue(currentProduct.getCategoryId(), productId);
        return related.stream()
                .limit(4)
                .map(this::buildProductResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductSpecificationDTO> getProductSpecifications(Long productId) {
        return specificationRepository.findByProductId(productId).stream()
                .map(s -> new ProductSpecificationDTO(s.getId(), s.getAttributeName(), s.getAttributeValue()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponseDTO createProduct(ProductCreateUpdateRequestDTO request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setBrandId(request.getBrandId());
        product.setCategoryId(request.getCategoryId());
        product.setModelNumber(request.getModelNumber());
        product.setDescription(request.getDescription());
        product.setImageUrl(request.getImageUrl());
        product.setProductIdentifier(request.getProductIdentifier());
        product.setActive(request.getActive() != null ? request.getActive() : true);

        Product saved = productRepository.save(product);

        if (request.getSpecifications() != null) {
            for (ProductSpecificationDTO spec : request.getSpecifications()) {
                ProductSpecification entity = new ProductSpecification(null, saved.getId(), spec.getAttributeName(), spec.getAttributeValue());
                specificationRepository.save(entity);
            }
        }

        return buildProductResponse(saved);
    }

    @Transactional
    public ProductResponseDTO updateProduct(Long productId, ProductCreateUpdateRequestDTO request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (request.getName() != null) product.setName(request.getName());
        if (request.getBrandId() != null) product.setBrandId(request.getBrandId());
        if (request.getCategoryId() != null) product.setCategoryId(request.getCategoryId());
        if (request.getModelNumber() != null) product.setModelNumber(request.getModelNumber());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getImageUrl() != null) product.setImageUrl(request.getImageUrl());
        if (request.getProductIdentifier() != null) product.setProductIdentifier(request.getProductIdentifier());
        if (request.getActive() != null) product.setActive(request.getActive());

        Product updated = productRepository.save(product);

        if (request.getSpecifications() != null) {
            specificationRepository.deleteByProductId(productId);
            for (ProductSpecificationDTO spec : request.getSpecifications()) {
                ProductSpecification entity = new ProductSpecification(null, updated.getId(), spec.getAttributeName(), spec.getAttributeValue());
                specificationRepository.save(entity);
            }
        }

        return buildProductResponse(updated);
    }

    private ProductResponseDTO buildProductResponse(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setBrandId(product.getBrandId());
        dto.setCategoryId(product.getCategoryId());
        dto.setModelNumber(product.getModelNumber());
        dto.setDescription(product.getDescription());
        dto.setImageUrl(product.getImageUrl());
        dto.setProductIdentifier(product.getProductIdentifier());
        dto.setActive(product.getActive());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());

        if (product.getBrandId() != null) {
            brandRepository.findById(product.getBrandId()).ifPresent(b -> dto.setBrandName(b.getName()));
        }
        if (product.getCategoryId() != null) {
            categoryRepository.findById(product.getCategoryId()).ifPresent(c -> dto.setCategoryName(c.getName()));
        }

        // Lowest price & offer count
        List<ProductOffer> offers = offerRepository.findByProductIdAndActiveTrue(product.getId());
        dto.setOfferCount(offers.size());
        BigDecimal lowest = offers.stream()
                .map(ProductOffer::getCurrentPrice)
                .filter(Objects::nonNull)
                .min(BigDecimal::compareTo)
                .orElse(null);
        dto.setLowestPrice(lowest);

        // Specifications
        List<ProductSpecificationDTO> specs = specificationRepository.findByProductId(product.getId()).stream()
                .map(s -> new ProductSpecificationDTO(s.getId(), s.getAttributeName(), s.getAttributeValue()))
                .collect(Collectors.toList());
        dto.setSpecifications(specs);

        // Distinct product variants (same brand & category, but distinct model/storage)
        if (product.getBrandId() != null) {
            List<Product> sameBrand = productRepository.findByCategoryIdAndIdNotAndActiveTrue(product.getCategoryId(), product.getId());
            List<ProductVariantDTO> variants = new ArrayList<>();
            for (Product sibling : sameBrand) {
                if (Objects.equals(sibling.getBrandId(), product.getBrandId())) {
                    BigDecimal sibLowest = offerRepository.findLowestPriceByProductId(sibling.getId()).orElse(null);
                    variants.add(new ProductVariantDTO(sibling.getId(), sibling.getName(), sibling.getModelNumber(), sibLowest));
                }
            }
            dto.setAvailableVariants(variants);
        }

        return dto;
    }
}

