package com.ecommerce.domain.service;

import com.ecommerce.domain.model.Price;
import com.ecommerce.domain.port.in.GetPriceUseCase;
import com.ecommerce.domain.port.out.PriceRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
public class GetPriceService implements GetPriceUseCase {

    private final PriceRepositoryPort priceRepositoryPort;

    @Override
    public Optional<Price> getPrice(LocalDateTime applicationDate, Long productId, Long brandId) {
        return priceRepositoryPort.findApplicablePrice(applicationDate, productId, brandId);
    }
}
