package com.pricewise;

import com.pricewise.util.PriceUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PriceUtilsTest {

    @Test
    @DisplayName("Discount calculation formula: ((original - current) / original) * 100")
    void testDiscountCalculationValid() {
        BigDecimal original = new BigDecimal("80000.00");
        BigDecimal current = new BigDecimal("60000.00");

        BigDecimal discount = PriceUtils.calculateDiscountPercentage(original, current);
        assertNotNull(discount);
        assertEquals(new BigDecimal("25.00"), discount);
    }

    @Test
    @DisplayName("Discount calculation returns null when original price is missing or invalid")
    void testDiscountCalculationInvalidOriginalPrice() {
        assertNull(PriceUtils.calculateDiscountPercentage(null, new BigDecimal("1000.00")));
        assertNull(PriceUtils.calculateDiscountPercentage(new BigDecimal("0.00"), new BigDecimal("1000.00")));
        assertNull(PriceUtils.calculateDiscountPercentage(new BigDecimal("-50.00"), new BigDecimal("1000.00")));
    }

    @Test
    @DisplayName("Discount calculation returns 0.00 when current price is greater than or equal to original")
    void testDiscountCalculationCurrentHigher() {
        BigDecimal original = new BigDecimal("5000.00");
        BigDecimal current = new BigDecimal("6000.00");

        BigDecimal discount = PriceUtils.calculateDiscountPercentage(original, current);
        assertNotNull(discount);
        assertEquals(new BigDecimal("0.00"), discount);
    }

    @Test
    @DisplayName("Total delivered price calculated when delivery charge is known")
    void testDeliveredPriceKnown() {
        BigDecimal current = new BigDecimal("48999.00");
        BigDecimal delivery = new BigDecimal("99.00");

        BigDecimal total = PriceUtils.calculateDeliveredPrice(current, delivery);
        assertNotNull(total);
        assertEquals(new BigDecimal("49098.00"), total);
    }

    @Test
    @DisplayName("Total delivered price is unknown (null) when delivery charge is null")
    void testDeliveredPriceUnknown() {
        BigDecimal current = new BigDecimal("48999.00");
        BigDecimal total = PriceUtils.calculateDeliveredPrice(current, null);
        assertNull(total, "Total delivered price must be unknown if delivery charge is not supplied");
    }
}

