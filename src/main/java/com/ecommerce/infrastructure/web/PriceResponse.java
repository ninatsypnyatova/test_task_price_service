package com.ecommerce.infrastructure.web;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Immutable response DTO returned by {@link PriceController}.
 *
 * <p>Carries all information the client needs about the applicable price tariff.</p>
 *
 * @param productId  the product identifier
 * @param brandId    the brand identifier
 * @param priceList  the tariff/price-list identifier that applies
 * @param startDate  the start of the tariff validity period
 * @param endDate    the end of the tariff validity period
 * @param price      the final sale price
 * @param currency   the ISO 4217 currency code (e.g. "EUR")
 */
public record PriceResponse(Long productId, Long brandId, Integer priceList,
                             LocalDateTime startDate, LocalDateTime endDate,
                             BigDecimal price, String currency) {
}
