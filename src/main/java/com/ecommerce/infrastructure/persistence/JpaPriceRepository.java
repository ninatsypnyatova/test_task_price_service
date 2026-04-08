package com.ecommerce.infrastructure.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for {@link PriceEntity}.
 *
 * <p>Provides a custom JPQL query that retrieves active price tariffs ordered by
 * priority in descending order. Using a {@link Pageable} parameter allows the caller
 * to efficiently limit results to the top-priority entry without loading the full set.</p>
 */
public interface JpaPriceRepository extends JpaRepository<PriceEntity, Long> {

    /**
     * Returns price entities whose validity period covers the given {@code applicationDate},
     * filtered by brand and product, and sorted by priority (highest first).
     *
     * @param brandId         the brand identifier to filter by
     * @param productId       the product identifier to filter by
     * @param applicationDate the date and time that must fall within {@code [startDate, endDate]}
     * @param pageable        pagination descriptor — typically {@code PageRequest.of(0, 1)}
     *                        to retrieve only the top-priority result
     * @return list of matching {@link PriceEntity} instances ordered by priority descending
     */
    @Query("SELECT p FROM PriceEntity p " +
           "WHERE p.brandId = :brandId " +
           "AND p.productId = :productId " +
           "AND p.startDate <= :applicationDate " +
           "AND p.endDate >= :applicationDate " +
           "ORDER BY p.priority DESC")
    List<PriceEntity> findTopByBrandIdAndProductIdAndDateRange(
            @Param("brandId") Long brandId,
            @Param("productId") Long productId,
            @Param("applicationDate") LocalDateTime applicationDate,
            Pageable pageable);
}
