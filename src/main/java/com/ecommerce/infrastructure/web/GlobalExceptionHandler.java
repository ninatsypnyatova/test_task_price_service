package com.ecommerce.infrastructure.web;

import com.ecommerce.domain.exception.InvalidPriceException;
import com.ecommerce.domain.exception.PriceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler that translates domain exceptions into appropriate HTTP responses.
 *
 * <p>Keeps exception-handling logic out of individual controllers and centralises the
 * mapping between domain exceptions and HTTP status codes.</p>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link PriceNotFoundException} and returns {@code 404 Not Found}.
     *
     * @param ex the exception thrown when no applicable price tariff is found
     */
    @ExceptionHandler(PriceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handlePriceNotFound(PriceNotFoundException ex) {
        log.warn("Price not found: {}", ex.getMessage());
    }

    /**
     * Handles {@link InvalidPriceException} and returns {@code 400 Bad Request}.
     *
     * @param ex the exception thrown when price-related input fails domain validation
     */
    @ExceptionHandler(InvalidPriceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleInvalidPrice(InvalidPriceException ex) {
        log.warn("Invalid price request: {}", ex.getMessage());
    }
}
