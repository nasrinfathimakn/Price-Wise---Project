package com.pricewise;

import com.pricewise.config.RetailerConfigProperties;
import com.pricewise.dto.PriceComparisonResponseDTO;
import com.pricewise.dto.ProductOfferResponseDTO;
import com.pricewise.entity.Product;
import com.pricewise.entity.ProductOffer;
import com.pricewise.entity.Retailer;
import com.pricewise.enums.AvailabilityStatus;
import com.pricewise.enums.IntegrationType;
import com.pricewise.repository.ProductOfferRepository;
import com.pricewise.repository.ProductRepository;
import com.pricewise.repository.RetailerRepository;
import com.pricewise.service.PriceComparisonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PriceComparisonServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductOfferRepository offerRepository;

    @Mock
    private RetailerRepository retailerRepository;

    private RetailerConfigProperties properties;
    private PriceComparisonService comparisonService;

    @BeforeEach
    void setUp() {
        properties = new RetailerConfigProperties();
        comparisonService = new PriceComparisonService(productRepository, offerRepository, retailerRepository, properties);
    }

    @Test
    @DisplayName("Identifies lowest verified price and marks offers correctly")
    void testLowestPriceIdentification() {
        Long productId = 1L;
        Product product = new Product(productId, "Samsung Galaxy S25", 2L, 2L, "SM-S931B", "Desc", "url", "8806095000012", true);

        Retailer r1 = new Retailer(1L, "Amazon India", "AMAZON_IN", "https://amazon.in", null, true, IntegrationType.MOCK);
        Retailer r2 = new Retailer(2L, "Flipkart", "FLIPKART", "https://flipkart.com", null, true, IntegrationType.MOCK);

        ProductOffer offer1 = new ProductOffer();
        offer1.setId(101L);
        offer1.setProductId(productId);
        offer1.setRetailerId(1L);
        offer1.setExternalProductId("AMZ-1");
        offer1.setExternalTitle("Samsung S25 on Amazon");
        offer1.setCurrentPrice(new BigDecimal("74999.00"));
        offer1.setOriginalPrice(new BigDecimal("79999.00"));
        offer1.setDeliveryCharge(BigDecimal.ZERO);
        offer1.setAvailabilityStatus(AvailabilityStatus.IN_STOCK);
        offer1.setLastFetchedAt(OffsetDateTime.now());
        offer1.setLastSuccessfulFetchAt(OffsetDateTime.now());
        offer1.setActive(true);

        ProductOffer offer2 = new ProductOffer();
        offer2.setId(102L);
        offer2.setProductId(productId);
        offer2.setRetailerId(2L);
        offer2.setExternalProductId("FLIP-1");
        offer2.setExternalTitle("Samsung S25 on Flipkart");
        offer2.setCurrentPrice(new BigDecimal("73999.00")); // Lowest
        offer2.setOriginalPrice(new BigDecimal("79999.00"));
        offer2.setDeliveryCharge(new BigDecimal("99.00"));
        offer2.setAvailabilityStatus(AvailabilityStatus.IN_STOCK);
        offer2.setLastFetchedAt(OffsetDateTime.now());
        offer2.setLastSuccessfulFetchAt(OffsetDateTime.now());
        offer2.setActive(true);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(offerRepository.findByProductIdAndActiveTrue(productId)).thenReturn(List.of(offer1, offer2));
        when(retailerRepository.findAll()).thenReturn(List.of(r1, r2));

        PriceComparisonResponseDTO response = comparisonService.compareProductOffers(productId);

        assertNotNull(response);
        assertEquals(new BigDecimal("73999.00"), response.getLowestVerifiedComparablePrice());
        assertEquals("Flipkart", response.getLowestOfferRetailerName());

        List<ProductOfferResponseDTO> offers = response.getAvailableOffers();
        assertEquals(2, offers.size());
        assertTrue(offers.get(0).getIsLowestPrice(), "First sorted offer should be marked as lowest price");
        assertEquals(new BigDecimal("73999.00"), offers.get(0).getCurrentPrice());
    }

    @Test
    @DisplayName("Handles stale offer transparently without misleading lowest claims")
    void testStaleOfferHandling() {
        Long productId = 1L;
        Product product = new Product(productId, "Samsung Galaxy S25", 2L, 2L, "SM-S931B", "Desc", "url", "8806095000012", true);

        Retailer r1 = new Retailer(1L, "Amazon India", "AMAZON_IN", "https://amazon.in", null, true, IntegrationType.MOCK);

        ProductOffer staleOffer = new ProductOffer();
        staleOffer.setId(101L);
        staleOffer.setProductId(productId);
        staleOffer.setRetailerId(1L);
        staleOffer.setExternalProductId("AMZ-1");
        staleOffer.setExternalTitle("Samsung S25 on Amazon");
        staleOffer.setCurrentPrice(new BigDecimal("74999.00"));
        staleOffer.setAvailabilityStatus(AvailabilityStatus.IN_STOCK);
        staleOffer.setLastFetchedAt(OffsetDateTime.now().minusDays(3));
        staleOffer.setLastSuccessfulFetchAt(OffsetDateTime.now().minusDays(3)); // 3 days old = stale
        staleOffer.setActive(true);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(offerRepository.findByProductIdAndActiveTrue(productId)).thenReturn(List.of(staleOffer));
        when(retailerRepository.findAll()).thenReturn(List.of(r1));

        PriceComparisonResponseDTO response = comparisonService.compareProductOffers(productId);

        assertEquals("PARTIALLY_STALE", response.getDataFreshnessStatus());
        assertTrue(response.getFreshnessExplanation().contains("hours"));
        assertTrue(response.getAvailableOffers().get(0).getIsStale());
    }
}

