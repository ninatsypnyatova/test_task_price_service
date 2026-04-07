package com.ecommerce.domain.port.in;

import com.ecommerce.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Optional;

public interface GetPriceUseCase {

    Optional<Price> getPrice(LocalDateTime applicationDate, Long productId, Long brandId);
}
