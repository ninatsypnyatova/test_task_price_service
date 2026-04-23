package com.ecommerce.infrastructure.persistence;

import com.ecommerce.domain.model.Price;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper that converts {@link PriceEntity} JPA entities into {@link Price} domain records.
 *
 * <p>The implementation is generated at compile time by the MapStruct annotation processor
 * and registered as a Spring bean via {@code componentModel = "spring"}.</p>
 */
@Mapper(componentModel = "spring")
public interface PriceEntityMapper {

    /**
     * Converts a {@link PriceEntity} to its domain model equivalent {@link Price}.
     *
     * @param entity the JPA entity to convert
     * @return the corresponding {@link Price} domain record
     */
    Price toDomain(PriceEntity entity);
}
