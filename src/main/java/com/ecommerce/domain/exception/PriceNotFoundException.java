package com.ecommerce.domain.exception;

import java.time.LocalDateTime;

/**
 * Thrown when no applicable price tariff is found for the requested combination of
 * product, brand, and date.
 */
public class PriceNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code PriceNotFoundException} with a message that includes
     * the key parameters used during the price lookup.
     *
     * @param productId       the product identifier that was queried
     * @param brandId         the brand identifier that was queried
     * @param applicationDate the date and time that was queried
     */
    public PriceNotFoundException(Long productId, Long brandId, LocalDateTime applicationDate) {
        super(String.format(
                "No applicable price found for productId=%d, brandId=%d at %s",
                productId, brandId, applicationDate));
    }
}
