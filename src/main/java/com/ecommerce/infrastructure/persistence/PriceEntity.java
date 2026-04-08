package com.ecommerce.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * JPA entity that maps to the {@code PRICES} database table.
 *
 * <p>This class is part of the infrastructure layer and is used exclusively by the
 * persistence adapter. It must not be referenced from the domain or application layers.</p>
 */
@Getter
@Entity
@Table(name = "PRICES")
public class PriceEntity {

    /** Required no-arg constructor for JPA. */
    protected PriceEntity() {
    }

    /** Auto-generated surrogate primary key. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The brand identifier (e.g. 1 = ZARA). */
    @Column(name = "BRAND_ID", nullable = false)
    private Long brandId;

    /** The start of the tariff validity period. */
    @Column(name = "START_DATE", nullable = false)
    private LocalDateTime startDate;

    /** The end of the tariff validity period. */
    @Column(name = "END_DATE", nullable = false)
    private LocalDateTime endDate;

    /** The tariff/price-list identifier. */
    @Column(name = "PRICE_LIST", nullable = false)
    private Integer priceList;

    /** The product identifier. */
    @Column(name = "PRODUCT_ID", nullable = false)
    private Long productId;

    /** Disambiguation priority — the highest value wins when tariffs overlap. */
    @Column(name = "PRIORITY", nullable = false)
    private Integer priority;

    /** The final sale price. */
    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    /** The ISO 4217 currency code (e.g. "EUR"). */
    @Column(name = "CURR", nullable = false, length = 3)
    private String currency;
}
