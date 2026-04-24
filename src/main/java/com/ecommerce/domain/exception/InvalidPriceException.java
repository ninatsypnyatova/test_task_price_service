package com.ecommerce.domain.exception;

/**
 * Thrown when a {@code Price} or price-related request fails domain validation.
 *
 * <p>Examples include a start date that is after the end date, a negative price amount,
 * or invalid/missing request parameters.</p>
 */
public class InvalidPriceException extends RuntimeException {

    /**
     * Constructs a new {@code InvalidPriceException} with the given detail message.
     *
     * @param message a human-readable description of the validation failure
     */
    public InvalidPriceException(String message) {
        super(message);
    }
}
