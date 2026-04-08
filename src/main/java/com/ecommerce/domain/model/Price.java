package com.ecommerce.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Immutable domain model representing a price tariff entry.
 *
 * <p>A {@code Price} holds all information about a single tariff, including the brand and
 * product it belongs to, the validity period, the price list identifier, the monetary amount,
 * and the priority used to resolve overlapping tariffs.</p>
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
public record Price(Long brandId, LocalDateTime startDate, LocalDateTime endDate,
                    Integer priceList, Long productId, Integer priority,
                    BigDecimal price, String currency) {
}
