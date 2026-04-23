package com.ecommerce;

import com.ecommerce.domain.port.in.GetPriceUseCase;
import com.ecommerce.domain.port.out.PriceRepositoryPort;
import com.ecommerce.application.service.GetPriceService;
import com.ecommerce.infrastructure.persistence.JpaPriceRepository;
import com.ecommerce.infrastructure.persistence.PriceEntityMapper;
import com.ecommerce.infrastructure.persistence.PriceRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring application configuration class responsible for wiring the hexagonal architecture beans.
 *
 * <p>This class manually declares the domain-layer beans so that the domain service and ports
 * remain free of Spring annotations, keeping the core business logic independent of the
 * framework.</p>
 */
@Configuration
public class ApplicationConfig {

    /**
     * Creates the {@link PriceRepositoryPort} output-port bean backed by the JPA repository.
     *
     * @param jpaPriceRepository the Spring Data JPA repository used by the adapter
     * @param priceEntityMapper  the mapper used to convert entities to domain objects
     * @return a {@link PriceRepositoryAdapter} that implements {@link PriceRepositoryPort}
     */
    @Bean
    public PriceRepositoryPort priceRepositoryPort(JpaPriceRepository jpaPriceRepository,
                                                   PriceEntityMapper priceEntityMapper) {
        return new PriceRepositoryAdapter(jpaPriceRepository, priceEntityMapper);
    }

    /**
     * Creates the {@link GetPriceUseCase} input-port bean backed by the domain service.
     *
     * @param priceRepositoryPort the output port used by the service to query prices
     * @return a {@link GetPriceService} that implements {@link GetPriceUseCase}
     */
    @Bean
    public GetPriceUseCase getPriceUseCase(PriceRepositoryPort priceRepositoryPort) {
        return new GetPriceService(priceRepositoryPort);
    }
}
