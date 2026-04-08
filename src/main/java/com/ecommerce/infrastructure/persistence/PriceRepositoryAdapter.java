package com.ecommerce.infrastructure.persistence;

import com.ecommerce.domain.model.Price;
import com.ecommerce.domain.port.out.PriceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Adapter that implements the {@link PriceRepositoryPort} output port using Spring Data JPA.
 *
 * <p>This class belongs to the infrastructure layer and acts as a bridge between the domain
 * model and the JPA persistence mechanism. It queries the database via
 * {@link JpaPriceRepository} and converts the result into a domain {@link Price} record.</p>
 */
@RequiredArgsConstructor
public class PriceRepositoryAdapter implements PriceRepositoryPort {

    private final JpaPriceRepository jpaPriceRepository;

    /**
     * {@inheritDoc}
     *
     * <p>Queries the database for all tariffs that are active at {@code applicationDate} for
     * the given product and brand, sorted by priority descending. Only the first result
     * (highest priority) is returned.</p>
     */
    @Override
    public Optional<Price> findApplicablePrice(LocalDateTime applicationDate, Long productId, Long brandId) {
        return jpaPriceRepository
                .findTopByBrandIdAndProductIdAndDateRange(brandId, productId, applicationDate, PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .map(this::toPrice);
    }

    /**
     * Converts a {@link PriceEntity} to its domain model equivalent {@link Price}.
     *
     * @param entity the JPA entity to convert
     * @return the corresponding {@link Price} domain record
     */
    private Price toPrice(PriceEntity entity) {
        return new Price(
                entity.getBrandId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getPriceList(),
                entity.getProductId(),
                entity.getPriority(),
                entity.getPrice(),
                entity.getCurrency()
        );
    }
}
