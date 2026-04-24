package com.ecommerce.domain.model;

/**
 * Value object representing a product identifier.
 *
 * <p>Wraps a raw {@code Long} to give it semantic meaning in the domain model,
 * preventing accidental mix-up with other {@code Long} identifiers.</p>
 *
 * @param value the raw numeric product identifier
 */
public record ProductId(Long value) {
}
