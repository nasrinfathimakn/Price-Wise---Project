package com.pricewise.service;

import com.pricewise.dto.WatchlistResponseDTO;
import com.pricewise.entity.Brand;
import com.pricewise.entity.Category;
import com.pricewise.entity.Product;
import com.pricewise.entity.ProductOffer;
import com.pricewise.entity.User;
import com.pricewise.entity.WatchlistItem;
import com.pricewise.exception.DuplicateResourceException;
import com.pricewise.exception.ResourceNotFoundException;
import com.pricewise.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class WatchlistService {

    private final WatchlistItemRepository watchlistRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductOfferRepository offerRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    public WatchlistService(
            WatchlistItemRepository watchlistRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            ProductOfferRepository offerRepository,
            BrandRepository brandRepository,
            CategoryRepository categoryRepository) {
        this.watchlistRepository = watchlistRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.offerRepository = offerRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public WatchlistResponseDTO addToWatchlist(String userEmail, Long productId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (watchlistRepository.existsByUserIdAndProductId(user.getId(), productId)) {
            throw new DuplicateResourceException("Product is already in your watchlist");
        }

        WatchlistItem item = new WatchlistItem(null, user.getId(), productId);
        WatchlistItem saved = watchlistRepository.save(item);

        return mapToDTO(saved, product);
    }

    @Transactional
    public void removeFromWatchlist(String userEmail, Long productId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        watchlistRepository.deleteByUserIdAndProductId(user.getId(), productId);
    }

    @Transactional(readOnly = true)
    public List<WatchlistResponseDTO> getUserWatchlist(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        List<WatchlistItem> items = watchlistRepository.findByUserId(user.getId());
        List<WatchlistResponseDTO> dtos = new ArrayList<>();

        for (WatchlistItem item : items) {
            productRepository.findById(item.getProductId())
                    .ifPresent(product -> dtos.add(mapToDTO(item, product)));
        }

        return dtos;
    }

    private WatchlistResponseDTO mapToDTO(WatchlistItem item, Product product) {
        WatchlistResponseDTO dto = new WatchlistResponseDTO();
        dto.setId(item.getId());
        dto.setUserId(item.getUserId());
        dto.setProductId(product.getId());
        dto.setProductName(product.getName());
        dto.setProductImageUrl(product.getImageUrl());
        dto.setCreatedAt(item.getCreatedAt());

        if (product.getBrandId() != null) {
            brandRepository.findById(product.getBrandId()).ifPresent(b -> dto.setBrandName(b.getName()));
        }
        if (product.getCategoryId() != null) {
            categoryRepository.findById(product.getCategoryId()).ifPresent(c -> dto.setCategoryName(c.getName()));
        }

        List<ProductOffer> offers = offerRepository.findByProductIdAndActiveTrue(product.getId());
        dto.setOfferCount(offers.size());
        BigDecimal lowest = offers.stream()
                .map(ProductOffer::getCurrentPrice)
                .filter(Objects::nonNull)
                .min(BigDecimal::compareTo)
                .orElse(null);
        dto.setLowestPrice(lowest);

        return dto;
    }
}

