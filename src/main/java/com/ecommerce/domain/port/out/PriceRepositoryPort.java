package com.ecommerce.domain.port.out;

import com.ecommerce.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Output port (secondary port) for querying prices from an external data source.
 *
 * <p>This interface abstracts the persistence mechanism from the domain layer.
 * Implementations live in the infrastructure package (e.g. {@code PriceRepositoryAdapter})
 * and are injected at runtime.</p>
 */
public interface PriceRepositoryPort {

    /**
     * Finds the applicable price tariff for the given product, brand, and point in time.
     *
     * <p>When multiple tariffs are active for the same product and brand, the implementation
     * must return only the one with the highest priority.</p>
     *
     * @param applicationDate the date and time to look up
     * @param productId       the product identifier
     * @param brandId         the brand identifier
     * @return an {@link Optional} containing the highest-priority matching {@link Price},
     *         or empty if no tariff is active at the requested date
     */
    Optional<Price> findApplicablePrice(LocalDateTime applicationDate, Long productId, Long brandId);
}
