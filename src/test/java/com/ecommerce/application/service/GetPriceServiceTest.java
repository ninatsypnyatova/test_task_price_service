package com.ecommerce.application.service;

import com.ecommerce.domain.model.Price;
import com.ecommerce.domain.port.out.PriceRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPriceServiceTest {

    @Mock
    private PriceRepositoryPort priceRepositoryPort;

    @InjectMocks
    private GetPriceService getPriceService;

    @Test
    void getPrice_whenPriceExists_returnsPrice() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;
        Price expected = new Price(brandId, date, date.plusDays(1), 1, productId, 0,
                new BigDecimal("35.50"), "EUR");

        when(priceRepositoryPort.findApplicablePrice(date, productId, brandId))
                .thenReturn(Optional.of(expected));

        Optional<Price> result = getPriceService.getPrice(date, productId, brandId);

        assertThat(result).isPresent().contains(expected);
    }

    @Test
    void getPrice_whenPriceNotExists_returnsEmpty() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 99999L;
        Long brandId = 99L;

        when(priceRepositoryPort.findApplicablePrice(date, productId, brandId))
                .thenReturn(Optional.empty());

        Optional<Price> result = getPriceService.getPrice(date, productId, brandId);

        assertThat(result).isEmpty();
    }
}
