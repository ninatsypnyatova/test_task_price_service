package com.ecommerce.application.service;

import com.ecommerce.application.validation.PriceRequestValidator;
import com.ecommerce.domain.model.Price;
import com.ecommerce.domain.port.in.GetPriceUseCase;
import com.ecommerce.domain.port.out.PriceRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Application service that implements the {@link GetPriceUseCase} input port.
 *
 * <p>This service delegates the price lookup to the {@link PriceRepositoryPort} output port,
 * keeping the domain logic free of infrastructure concerns. The repository is responsible
 * for returning only the highest-priority tariff when multiple tariffs overlap.</p>
 *
 * <p>Before querying, the service validates the incoming parameters via
 * {@link PriceRequestValidator} to reject invalid inputs early.</p>
 */
@Slf4j
@RequiredArgsConstructor
public class GetPriceService implements GetPriceUseCase {

    private final PriceRepositoryPort priceRepositoryPort;
    private final PriceRequestValidator priceRequestValidator;

    /**
     * {@inheritDoc}
     *
     * <p>Validates the request parameters, then delegates to
     * {@link PriceRepositoryPort#findApplicablePrice} and returns the result.</p>
     */
    @Override
    public Optional<Price> getPrice(LocalDateTime applicationDate, Long productId, Long brandId) {
        priceRequestValidator.validate(applicationDate, productId, brandId);
        log.debug("Querying applicable price: applicationDate={}, productId={}, brandId={}", applicationDate, productId, brandId);
        Optional<Price> result = priceRepositoryPort.findApplicablePrice(applicationDate, productId, brandId);
        log.debug("Price query result: {}", result.orElse(null));
        return result;
    }
}
