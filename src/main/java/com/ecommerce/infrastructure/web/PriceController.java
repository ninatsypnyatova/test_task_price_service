package com.ecommerce.infrastructure.web;

import com.ecommerce.domain.model.Price;
import com.ecommerce.domain.port.in.GetPriceUseCase;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/prices")
@RequiredArgsConstructor
@Validated
public class PriceController {

    private final GetPriceUseCase getPriceUseCase;
    private final PriceMapper priceMapper;

    @GetMapping
    public ResponseEntity<PriceResponse> getPrice(
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate,
            @RequestParam @NotNull Long productId,
            @RequestParam @NotNull Long brandId) {

        Optional<Price> price = getPriceUseCase.getPrice(applicationDate, productId, brandId);

        return price
                .map(p -> ResponseEntity.ok(priceMapper.toResponse(p)))
                .orElse(ResponseEntity.notFound().build());
    }
}
