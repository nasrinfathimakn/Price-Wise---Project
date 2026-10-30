package com.pricewise.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class PriceUtils {

    private PriceUtils() {
    }

    /**
     * Calculates discount percentage:
     * discountPercentage = ((originalPrice - currentPrice) / originalPrice) * 100
     * Returns BigDecimal with scale 2. Returns null when originalPrice is null, <= 0, or currentPrice >= originalPrice.
     */
    public static BigDecimal calculateDiscountPercentage(BigDecimal originalPrice, BigDecimal currentPrice) {
        if (originalPrice == null || currentPrice == null) {
            return null;
        }
        if (originalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        if (currentPrice.compareTo(originalPrice) >= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal difference = originalPrice.subtract(currentPrice);
        return difference.divide(originalPrice, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculates delivered price = currentPrice + deliveryCharge.
     * Returns null if deliveryCharge is unknown (null).
     */
    public static BigDecimal calculateDeliveredPrice(BigDecimal currentPrice, BigDecimal deliveryCharge) {
        if (currentPrice == null || deliveryCharge == null) {
            return null;
        }
        return currentPrice.add(deliveryCharge).setScale(2, RoundingMode.HALF_UP);
    }
}

