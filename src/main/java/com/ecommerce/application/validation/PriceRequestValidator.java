package com.ecommerce.application.validation;

import com.ecommerce.domain.exception.InvalidPriceException;

import java.time.LocalDateTime;

/**
 * Application-layer component that validates the inputs of a price query request.
 *
 * <p>This validator is invoked by the application service before delegating to the
 * repository, ensuring that invalid inputs are rejected with a meaningful error message
 * before any persistence call is made.</p>
 */
public class PriceRequestValidator {

    /**
     * Validates the parameters of a price query request.
     *
     * @param applicationDate the date and time to query — must not be {@code null}
     * @param productId       the product identifier — must be greater than zero
     * @param brandId         the brand identifier — must be greater than zero
     * @throws InvalidPriceException if any parameter fails validation
     */
    public void validate(LocalDateTime applicationDate, Long productId, Long brandId) {
        if (applicationDate == null) {
            throw new InvalidPriceException("applicationDate must not be null");
        }
        if (productId == null || productId <= 0) {
            throw new InvalidPriceException("productId must be greater than zero");
        }
        if (brandId == null || brandId <= 0) {
            throw new InvalidPriceException("brandId must be greater than zero");
        }
    }
}
