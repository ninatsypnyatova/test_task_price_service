package com.ecommerce.domain.port.out;

import com.ecommerce.domain.model.Price;

import java.util.List;

/**
 * Output port (secondary port) for querying prices from an external data source.
 *
 * <p>This interface abstracts the persistence mechanism from the domain layer.
 * Implementations live in the infrastructure package (e.g. {@code PriceRepositoryAdapter})
 * and are injected at runtime.</p>
 */
public interface PriceRepositoryPort {

    /**
     * Returns all price tariffs for the given product and brand.
     *
     * <p>No date filtering or priority ordering is applied — that logic belongs
     * in the application service layer.</p>
     *
     * @param productId the product identifier
     * @param brandId   the brand identifier
     * @return list of all {@link Price} records for the given product and brand
     */
    List<Price> findByProductAndBrand(Long productId, Long brandId);
}
