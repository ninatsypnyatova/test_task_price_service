package com.ecommerce.infrastructure.web;

import com.ecommerce.domain.model.Price;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceMapper {

    PriceResponse toResponse(Price price);
}
