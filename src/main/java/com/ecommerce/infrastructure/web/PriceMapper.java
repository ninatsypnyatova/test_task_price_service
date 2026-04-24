package com.ecommerce.infrastructure.web;

import com.ecommerce.domain.model.Price;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper that converts domain {@link Price} records into {@link PriceResponse} DTOs.
 *
 * <p>The implementation is generated at compile time by the MapStruct annotation processor
 * and registered as a Spring bean via {@code componentModel = "spring"}.</p>
 */
@Mapper(componentModel = "spring")
public interface PriceMapper {

    /**
     * Converts a domain {@link Price} record to a {@link PriceResponse} DTO.
     *
     * @param price the domain price record to convert
     * @return the corresponding {@link PriceResponse}
     */
    @Mapping(source = "brandId.value", target = "brandId")
    @Mapping(source = "productId.value", target = "productId")
    PriceResponse toResponse(Price price);
}
