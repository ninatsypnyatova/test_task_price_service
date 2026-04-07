package com.ecommerce.infrastructure.persistence;

import com.ecommerce.domain.model.Price;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceRepositoryAdapterTest {

    @Mock
    private JpaPriceRepository jpaPriceRepository;

    @InjectMocks
    private PriceRepositoryAdapter priceRepositoryAdapter;

    @Test
    void findApplicablePrice_whenEntityExists_returnsMappedPrice() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        PriceEntity entity = mock(PriceEntity.class);
        when(entity.getBrandId()).thenReturn(brandId);
        when(entity.getStartDate()).thenReturn(date);
        when(entity.getEndDate()).thenReturn(date.plusDays(1));
        when(entity.getPriceList()).thenReturn(1);
        when(entity.getProductId()).thenReturn(productId);
        when(entity.getPriority()).thenReturn(0);
        when(entity.getPrice()).thenReturn(new BigDecimal("35.50"));
        when(entity.getCurrency()).thenReturn("EUR");

        when(jpaPriceRepository.findTopByBrandIdAndProductIdAndDateRange(
                eq(brandId), eq(productId), eq(date), any(PageRequest.class)))
                .thenReturn(List.of(entity));

        Optional<Price> result = priceRepositoryAdapter.findApplicablePrice(date, productId, brandId);

        assertThat(result).isPresent();
        Price price = result.get();
        assertThat(price.brandId()).isEqualTo(brandId);
        assertThat(price.productId()).isEqualTo(productId);
        assertThat(price.priceList()).isEqualTo(1);
        assertThat(price.price()).isEqualByComparingTo("35.50");
        assertThat(price.currency()).isEqualTo("EUR");
    }

    @Test
    void findApplicablePrice_whenNoEntityExists_returnsEmpty() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 99999L;
        Long brandId = 99L;

        when(jpaPriceRepository.findTopByBrandIdAndProductIdAndDateRange(
                eq(brandId), eq(productId), eq(date), any(PageRequest.class)))
                .thenReturn(Collections.emptyList());

        Optional<Price> result = priceRepositoryAdapter.findApplicablePrice(date, productId, brandId);

        assertThat(result).isEmpty();
    }
}
