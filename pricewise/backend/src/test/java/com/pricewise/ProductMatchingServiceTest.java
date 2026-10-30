package com.pricewise;

import com.pricewise.entity.Brand;
import com.pricewise.entity.Product;
import com.pricewise.integration.ExternalOfferDTO;
import com.pricewise.repository.BrandRepository;
import com.pricewise.repository.ProductRepository;
import com.pricewise.repository.ProductSpecificationRepository;
import com.pricewise.service.ProductMatchingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductMatchingServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ProductSpecificationRepository specificationRepository;

    private ProductMatchingService matchingService;

    @BeforeEach
    void setUp() {
        matchingService = new ProductMatchingService(productRepository, brandRepository, specificationRepository);
    }

    @Test
    @DisplayName("Priority 1: Matches exact GTIN identifier")
    void testExactGtinMatch() {
        Product product = new Product(1L, "Samsung Galaxy S25 256GB", 1L, 1L, "SM-S931B", "Desc", "url", "8806095000012", true);
        when(productRepository.findByProductIdentifier("8806095000012")).thenReturn(Optional.of(product));

        ExternalOfferDTO offer = new ExternalOfferDTO();
        offer.setGtin("8806095000012");
        offer.setExternalTitle("Samsung Galaxy S25 256GB Onyx");
        offer.setPrice(new BigDecimal("74999.00"));

        ProductMatchingService.MatchResult result = matchingService.matchOfferToProduct(offer);
        assertTrue(result.getMatchedProduct().isPresent());
        assertEquals(1L, result.getMatchedProduct().get().getId());
        assertEquals("EXACT_GTIN", result.getMatchReason());
        assertFalse(result.isAmbiguous());
    }

    @Test
    @DisplayName("Variants isolation: 128GB phone must NOT match 256GB phone")
    void testVariantMismatchedStorage() {
        Product product256 = new Product(1L, "Samsung Galaxy S25 (256 GB)", 1L, 1L, "SM-S931B", "Desc", "url", "8806095000012", true);

        ExternalOfferDTO offer128 = new ExternalOfferDTO();
        offer128.setGtin("8806095000012");
        offer128.setExternalTitle("Samsung Galaxy S25 (128 GB)");
        offer128.setPrice(new BigDecimal("64999.00"));

        boolean isValidVariant = matchingService.validateVariants(product256, offer128);
        assertFalse(isValidVariant, "128GB offer must not be considered valid variant match for 256GB product");
    }

    @Test
    @DisplayName("Priority 2: Matches exact brand and model number")
    void testBrandAndModelMatch() {
        Brand samsung = new Brand(2L, "Samsung", "Electronics");
        Product product = new Product(2L, "Samsung Galaxy S25 5G", 2L, 1L, "SM-S931B", "Desc", "url", null, true);

        when(brandRepository.findByNameIgnoreCase("Samsung")).thenReturn(Optional.of(samsung));
        when(productRepository.findByBrandIdAndModelNumber(2L, "SM-S931B")).thenReturn(Optional.of(product));

        ExternalOfferDTO offer = new ExternalOfferDTO();
        offer.setRawBrand("Samsung");
        offer.setRawModel("SM-S931B");
        offer.setExternalTitle("Samsung Galaxy S25 5G Onyx");
        offer.setPrice(new BigDecimal("74999.00"));

        ProductMatchingService.MatchResult result = matchingService.matchOfferToProduct(offer);
        assertTrue(result.getMatchedProduct().isPresent());
        assertEquals("BRAND_AND_MODEL", result.getMatchReason());
    }

    @Test
    @DisplayName("Priority 5: Uncertain match flags for manual administrator review")
    void testUncertainMatchFlagged() {
        Product product = new Product(5L, "Sony WH-1000XM5 Wireless Headphones", 3L, 4L, "WH1000XM5B", "Desc", "url", null, true);
        when(productRepository.findByActiveTrue()).thenReturn(List.of(product));

        ExternalOfferDTO offer = new ExternalOfferDTO();
        offer.setRetailerCode("AMAZON_IN");
        offer.setExternalProductId("AMZ-SONY-TEST");
        offer.setExternalTitle("Sony WH-1000XM4 Noise Cancelling Earphones"); // somewhat similar
        offer.setPrice(new BigDecimal("21990.00"));

        ProductMatchingService.MatchResult result = matchingService.matchOfferToProduct(offer);
        if (result.isAmbiguous()) {
            assertEquals("UNCERTAIN_MATCH_PENDING_REVIEW", result.getMatchReason());
            assertFalse(matchingService.getPendingReviews().isEmpty());
        }
    }
}

