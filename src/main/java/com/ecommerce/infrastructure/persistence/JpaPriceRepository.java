package com.ecommerce.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link PriceEntity}.
 *
 * <p>Uses a Spring Data derived query to retrieve all price tariffs for a given
 * product and brand, without any date filtering or priority ordering.
 * Business logic such as date validation and priority selection belongs in the
 * application service layer.</p>
 */
public interface JpaPriceRepository extends JpaRepository<PriceEntity, Long> {

    /**
     * Returns all price entities for the given product and brand.
     *
     * @param productId the product identifier to filter by
     * @param brandId   the brand identifier to filter by
     * @return list of matching {@link PriceEntity} instances
     */
    List<PriceEntity> findByProductIdAndBrandId(Long productId, Long brandId);
}
