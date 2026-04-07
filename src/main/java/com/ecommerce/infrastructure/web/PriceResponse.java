package com.ecommerce.infrastructure.web;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceResponse(Long productId, Long brandId, Integer priceList,
                             LocalDateTime startDate, LocalDateTime endDate,
                             BigDecimal price, String currency) {
}
