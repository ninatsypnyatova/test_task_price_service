package com.ecommerce.infrastructure.web;

import com.ecommerce.domain.model.Price;
import com.ecommerce.domain.port.in.GetPriceUseCase;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * REST controller that exposes the price query API.
 *
 * <p>Handles {@code GET /api/v1/prices} requests and delegates to the
 * {@link GetPriceUseCase} domain use case. Responds with {@code 200 OK} and a
 * {@link PriceResponse} body when a matching tariff is found, or {@code 404 Not Found}
 * when no applicable tariff exists.</p>
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/prices")
@RequiredArgsConstructor
@Validated
public class PriceController {

    private final GetPriceUseCase getPriceUseCase;
    private final PriceMapper priceMapper;

    /**
     * Returns the applicable price tariff for the given product, brand, and date/time.
     *
     * @param applicationDate the ISO-8601 date-time at which the price should be looked up
     *                        (e.g. {@code 2020-06-14T10:00:00})
     * @param productId       the product identifier
     * @param brandId         the brand identifier (e.g. 1 = ZARA)
     * @return {@code 200 OK} with a {@link PriceResponse} body, or {@code 404 Not Found}
     *         if no tariff is active for the given combination of parameters
     */
    @GetMapping
    public ResponseEntity<PriceResponse> getPrice(
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate,
            @RequestParam @NotNull Long productId,
            @RequestParam @NotNull Long brandId) {

        log.info("Received price query: applicationDate={}, productId={}, brandId={}", applicationDate, productId, brandId);

        Optional<Price> price = getPriceUseCase.getPrice(applicationDate, productId, brandId);

        if (price.isPresent()) {
            log.debug("Price found for productId={}, brandId={}: {}", productId, brandId, price.get());
        } else {
            log.warn("No price found for productId={}, brandId={}, applicationDate={}", productId, brandId, applicationDate);
        }

        return price
                .map(p -> ResponseEntity.ok(priceMapper.toResponse(p)))
                .orElse(ResponseEntity.notFound().build());
    }
}
