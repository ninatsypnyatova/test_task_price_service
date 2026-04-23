package com.ecommerce.infrastructure.persistence;

import com.ecommerce.domain.model.Price;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceRepositoryAdapterTest {

    @Mock
    private JpaPriceRepository jpaPriceRepository;

    @Mock
    private PriceEntityMapper priceEntityMapper;

    @InjectMocks
    private PriceRepositoryAdapter priceRepositoryAdapter;

    @Test
    void findByProductAndBrand_whenEntitiesExist_returnsMappedPrices() {
        Long productId = 35455L;
        Long brandId = 1L;
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);

        PriceEntity entity = mock(PriceEntity.class);
        Price expectedPrice = new Price(brandId, date, date.plusDays(1), 1, productId, 0,
                new BigDecimal("35.50"), "EUR");

        when(jpaPriceRepository.findByProductIdAndBrandId(productId, brandId))
                .thenReturn(List.of(entity));
        when(priceEntityMapper.toDomain(entity)).thenReturn(expectedPrice);

        List<Price> result = priceRepositoryAdapter.findByProductAndBrand(productId, brandId);

        assertThat(result).hasSize(1);
        Price price = result.get(0);
        assertThat(price.brandId()).isEqualTo(brandId);
        assertThat(price.productId()).isEqualTo(productId);
        assertThat(price.priceList()).isEqualTo(1);
        assertThat(price.price()).isEqualByComparingTo("35.50");
        assertThat(price.currency()).isEqualTo("EUR");
    }

    @Test
    void findByProductAndBrand_whenNoEntitiesExist_returnsEmptyList() {
        Long productId = 99999L;
        Long brandId = 99L;

        when(jpaPriceRepository.findByProductIdAndBrandId(productId, brandId))
                .thenReturn(Collections.emptyList());

        List<Price> result = priceRepositoryAdapter.findByProductAndBrand(productId, brandId);

        assertThat(result).isEmpty();
    }
}
