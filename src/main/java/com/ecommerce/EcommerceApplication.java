package com.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the E-commerce Price Query Service.
 *
 * <p>This Spring Boot application exposes a REST API that returns the applicable sale price
 * for a product of a given brand at a specific point in time. When multiple price tariffs
 * overlap, the one with the highest priority wins.</p>
 */
@SpringBootApplication
public class EcommerceApplication {

    /**
     * Starts the Spring Boot application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }
}
