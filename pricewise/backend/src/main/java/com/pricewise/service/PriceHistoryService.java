package com.pricewise.service;

import com.pricewise.dto.PriceHistoryResponseDTO;
import com.pricewise.entity.PriceHistory;
import com.pricewise.entity.Product;
import com.pricewise.entity.ProductOffer;
import com.pricewise.entity.Retailer;
import com.pricewise.exception.ResourceNotFoundException;
import com.pricewise.repository.PriceHistoryRepository;
import com.pricewise.repository.ProductOfferRepository;
import com.pricewise.repository.ProductRepository;
import com.pricewise.repository.RetailerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class PriceHistoryService {

    private final PriceHistoryRepository priceHistoryRepository;
    private final ProductOfferRepository productOfferRepository;
    private final ProductRepository productRepository;
    private final RetailerRepository retailerRepository;

    public PriceHistoryService(
            PriceHistoryRepository priceHistoryRepository,
            ProductOfferRepository productOfferRepository,
            ProductRepository productRepository,
            RetailerRepository retailerRepository) {
        this.priceHistoryRepository = priceHistoryRepository;
        this.productOfferRepository = productOfferRepository;
        this.productRepository = productRepository;
        this.retailerRepository = retailerRepository;
    }

    @Transactional
    public PriceHistory savePriceObservation(Long productOfferId, BigDecimal price, BigDecimal deliveryCharge, String dataSource) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            // Do not store invalid or null price observation
            return null;
        }

        PriceHistory observation = new PriceHistory();
        observation.setProductOfferId(productOfferId);
        observation.setPrice(price);
        observation.setDeliveryCharge(deliveryCharge);
        observation.setCurrency("INR");
        observation.setObservedAt(OffsetDateTime.now());
        observation.setDataSource(dataSource != null ? dataSource : "MOCK");

        return priceHistoryRepository.save(observation);
    }

    @Transactional(readOnly = true)
    public PriceHistoryResponseDTO getProductPriceHistory(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        List<ProductOffer> offers = productOfferRepository.findByProductId(productId);
        PriceHistoryResponseDTO response = new PriceHistoryResponseDTO();
        response.setProductId(productId);
        response.setProductName(product.getName());
        response.setCurrency("INR");

        if (offers.isEmpty()) {
            response.setIsHistoryAvailable(false);
            return response;
        }

        Map<Long, String> retailerNameMap = new HashMap<>();
        for (Retailer r : retailerRepository.findAll()) {
            retailerNameMap.put(r.getId(), r.getName());
        }

        Map<Long, String> offerToRetailerMap = new HashMap<>();
        List<Long> offerIds = new ArrayList<>();
        for (ProductOffer offer : offers) {
            offerIds.add(offer.getId());
            offerToRetailerMap.put(offer.getId(), retailerNameMap.getOrDefault(offer.getRetailerId(), "Retailer"));
        }

        List<PriceHistory> historyList = priceHistoryRepository.findByProductOfferIdInOrderByObservedAtAsc(offerIds);
        if (historyList.isEmpty()) {
            response.setIsHistoryAvailable(false);
            return response;
        }

        response.setIsHistoryAvailable(true);
        BigDecimal min = null;
        BigDecimal max = null;
        BigDecimal latest = null;

        List<PriceHistoryResponseDTO.PriceHistoryPointDTO> points = new ArrayList<>();
        for (PriceHistory h : historyList) {
            String retailerName = offerToRetailerMap.getOrDefault(h.getProductOfferId(), "Retailer");
            PriceHistoryResponseDTO.PriceHistoryPointDTO point = new PriceHistoryResponseDTO.PriceHistoryPointDTO(
                    h.getId(),
                    h.getProductOfferId(),
                    retailerName,
                    h.getPrice(),
                    h.getDeliveryCharge(),
                    h.getObservedAt()
            );
            points.add(point);

            if (min == null || h.getPrice().compareTo(min) < 0) {
                min = h.getPrice();
            }
            if (max == null || h.getPrice().compareTo(max) > 0) {
                max = h.getPrice();
            }
            latest = h.getPrice();
        }

        response.setLowestRecordedPrice(min);
        response.setHighestRecordedPrice(max);
        response.setLatestRecordedPrice(latest);
        response.setObservations(points);

        return response;
    }
}

