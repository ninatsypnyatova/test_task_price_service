package com.ecommerce.application.service;

import com.ecommerce.domain.model.Price;
import com.ecommerce.domain.port.in.GetPriceUseCase;
import com.ecommerce.domain.port.out.PriceRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

/**
 * Application service that implements the {@link GetPriceUseCase} input port.
 *
 * <p>This service fetches all price tariffs for the requested product and brand from
 * the {@link PriceRepositoryPort} output port, then applies business logic to select
 * the tariff that is active at the given date/time and has the highest priority.</p>
 */
@Slf4j
@RequiredArgsConstructor
public class GetPriceService implements GetPriceUseCase {

    private final PriceRepositoryPort priceRepositoryPort;

    /**
     * {@inheritDoc}
     *
     * <p>Retrieves all tariffs for the given product and brand, filters those whose
     * validity period covers {@code applicationDate}, and returns the one with the
     * highest priority. Returns empty if no matching tariff is found.</p>
     */
    @Override
    public Optional<Price> getPrice(LocalDateTime applicationDate, Long productId, Long brandId) {
        log.debug("Querying applicable price: applicationDate={}, productId={}, brandId={}", applicationDate, productId, brandId);
        Optional<Price> result = priceRepositoryPort.findByProductAndBrand(productId, brandId)
                .stream()
                .filter(p -> !applicationDate.isBefore(p.startDate()) && !applicationDate.isAfter(p.endDate()))
                .max(Comparator.comparingInt(Price::priority));
        log.debug("Price query result: {}", result.orElse(null));
        return result;
    }
}
