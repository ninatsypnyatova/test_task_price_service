package com.ecommerce;

import com.ecommerce.domain.port.in.GetPriceUseCase;
import com.ecommerce.domain.port.out.PriceRepositoryPort;
import com.ecommerce.application.service.GetPriceService;
import com.ecommerce.infrastructure.persistence.JpaPriceRepository;
import com.ecommerce.infrastructure.persistence.PriceRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public PriceRepositoryPort priceRepositoryPort(JpaPriceRepository jpaPriceRepository) {
        return new PriceRepositoryAdapter(jpaPriceRepository);
    }

    @Bean
    public GetPriceUseCase getPriceUseCase(PriceRepositoryPort priceRepositoryPort) {
        return new GetPriceService(priceRepositoryPort);
    }
}
