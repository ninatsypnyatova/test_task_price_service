package com.ecommerce.infrastructure.persistence;

import com.ecommerce.domain.model.Price;
import com.ecommerce.domain.port.out.PriceRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Adapter that implements the {@link PriceRepositoryPort} output port using Spring Data JPA.
 *
 * <p>This class belongs to the infrastructure layer and acts as a bridge between the domain
 * model and the JPA persistence mechanism. It queries the database via
 * {@link JpaPriceRepository} and converts the results into domain {@link Price} records
 * using the {@link PriceEntityMapper}.</p>
 */
@Slf4j
@RequiredArgsConstructor
public class PriceRepositoryAdapter implements PriceRepositoryPort {

    private final JpaPriceRepository jpaPriceRepository;
    private final PriceEntityMapper priceEntityMapper;

    /**
     * {@inheritDoc}
     *
     * <p>Queries the database for all tariffs for the given product and brand
     * and maps each entity to a domain {@link Price} record.</p>
     */
    @Override
    public List<Price> findByProductAndBrand(Long productId, Long brandId) {
        log.debug("Fetching prices from repository: productId={}, brandId={}", productId, brandId);
        List<Price> result = jpaPriceRepository.findByProductIdAndBrandId(productId, brandId)
                .stream()
                .map(priceEntityMapper::toDomain)
                .toList();
        log.debug("Repository returned {} price(s)", result.size());
        return result;
    }
}
