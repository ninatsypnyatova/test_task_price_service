package com.ecommerce.domain.port.out;

import com.ecommerce.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PriceRepositoryPort {

    Optional<Price> findApplicablePrice(LocalDateTime applicationDate, Long productId, Long brandId);
}
