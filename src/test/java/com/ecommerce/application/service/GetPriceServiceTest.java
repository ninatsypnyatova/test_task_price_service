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
import java.util.Collections;
import java.util.List;
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
    void getPrice_whenOneMatchingTariff_returnsIt() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;
        Price price = new Price(brandId, date.minusDays(1), date.plusDays(1), 1, productId, 0,
                new BigDecimal("35.50"), "EUR");

        when(priceRepositoryPort.findByProductAndBrand(productId, brandId))
                .thenReturn(List.of(price));

        Optional<Price> result = getPriceService.getPrice(date, productId, brandId);

        assertThat(result).isPresent().contains(price);
    }

    @Test
    void getPrice_whenMultipleOverlappingTariffs_returnsHighestPriority() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 16, 0);
        Long productId = 35455L;
        Long brandId = 1L;
        Price lowPriority = new Price(brandId, date.minusDays(1), date.plusDays(1), 1, productId, 0,
                new BigDecimal("35.50"), "EUR");
        Price highPriority = new Price(brandId, date.minusHours(1), date.plusHours(1), 2, productId, 1,
                new BigDecimal("25.45"), "EUR");

        when(priceRepositoryPort.findByProductAndBrand(productId, brandId))
                .thenReturn(List.of(lowPriority, highPriority));

        Optional<Price> result = getPriceService.getPrice(date, productId, brandId);

        assertThat(result).isPresent().contains(highPriority);
    }

    @Test
    void getPrice_whenNoTariffMatchesDate_returnsEmpty() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;
        Price price = new Price(brandId, date.plusDays(1), date.plusDays(2), 1, productId, 0,
                new BigDecimal("35.50"), "EUR");

        when(priceRepositoryPort.findByProductAndBrand(productId, brandId))
                .thenReturn(List.of(price));

        Optional<Price> result = getPriceService.getPrice(date, productId, brandId);

        assertThat(result).isEmpty();
    }

    @Test
    void getPrice_whenNoPricesForProductAndBrand_returnsEmpty() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 99999L;
        Long brandId = 99L;

        when(priceRepositoryPort.findByProductAndBrand(productId, brandId))
                .thenReturn(Collections.emptyList());

        Optional<Price> result = getPriceService.getPrice(date, productId, brandId);

        assertThat(result).isEmpty();
    }
}
