package com.ecommerce.domain.port.in;

import com.ecommerce.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Input port (use case) for retrieving the applicable price for a product.
 *
 * <p>This interface is the primary entry point into the domain layer. It is implemented by the
 * application service and called by the inbound adapters (e.g. the REST controller).</p>
 */
public interface GetPriceUseCase {

    /**
     * Returns the applicable price tariff for a given product, brand, and point in time.
     *
     * <p>If multiple tariffs are active at the requested date, the one with the highest
     * {@code priority} value is returned. If no tariff is found, an empty {@link Optional}
     * is returned.</p>
     *
     * @param applicationDate the date and time to look up
     * @param productId       the product identifier
     * @param brandId         the brand identifier
     * @return an {@link Optional} containing the applicable {@link Price}, or empty if not found
     */
    Optional<Price> getPrice(LocalDateTime applicationDate, Long productId, Long brandId);
}
