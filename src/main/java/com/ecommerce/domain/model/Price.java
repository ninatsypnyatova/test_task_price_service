package com.ecommerce.domain.model;

import com.ecommerce.domain.exception.InvalidPriceException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Immutable domain model representing a price tariff entry.
 *
 * <p>A {@code Price} holds all information about a single tariff, including the brand and
 * product it belongs to, the validity period, the price list identifier, the monetary amount,
 * and the priority used to resolve overlapping tariffs.</p>
 *
 * <p>The compact constructor validates all business invariants on construction:
 * {@code startDate} must not be after {@code endDate}, {@code priority} must be &ge; 0,
 * and {@code price} must be &ge; 0.</p>
 *
 * @param brandId   the brand identifier (e.g. 1 = ZARA)
 * @param startDate the start of the tariff validity period
 * @param endDate   the end of the tariff validity period
 * @param priceList the tariff/price-list identifier
 * @param productId the product identifier
 * @param priority  disambiguation value — the highest numeric value wins when tariffs overlap
 * @param price     the final sale price
 * @param currency  the ISO 4217 currency code (e.g. "EUR")
 */
public record Price(BrandId brandId, LocalDateTime startDate, LocalDateTime endDate,
                    Integer priceList, ProductId productId, Integer priority,
                    BigDecimal price, String currency) {

    /** Compact constructor that enforces domain invariants. */
    public Price {
        Objects.requireNonNull(brandId, "brandId must not be null");
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(startDate, "startDate must not be null");
        Objects.requireNonNull(endDate, "endDate must not be null");
        Objects.requireNonNull(price, "price must not be null");
        if (startDate.isAfter(endDate)) {
            throw new InvalidPriceException("startDate must be before or equal to endDate");
        }
        if (priority < 0) {
            throw new InvalidPriceException("priority must be >= 0");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException("price must be >= 0");
        }
    }

    /**
     * Returns {@code true} if this tariff is active at the given date and time.
     *
     * @param date the point in time to check
     * @return {@code true} when {@code date} falls within {@code [startDate, endDate]}
     */
    public boolean isApplicable(LocalDateTime date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * Returns {@code true} if this tariff is a promotional price (i.e. has a non-zero priority).
     *
     * @return {@code true} when {@code priority > 0}
     */
    public boolean isPromotional() {
        return priority > 0;
    }
}
