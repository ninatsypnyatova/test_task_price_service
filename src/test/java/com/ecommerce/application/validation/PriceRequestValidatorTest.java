package com.ecommerce.application.validation;

import com.ecommerce.domain.exception.InvalidPriceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceRequestValidatorTest {

    private static final LocalDateTime VALID_DATE = LocalDateTime.of(2020, 6, 14, 10, 0);
    private static final Long VALID_PRODUCT_ID = 35455L;
    private static final Long VALID_BRAND_ID = 1L;

    private PriceRequestValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PriceRequestValidator();
    }

    @Nested
    class ValidInput {

        @Test
        void shouldPassWhenAllParametersAreValid() {
            assertThatNoException()
                    .isThrownBy(() -> validator.validate(VALID_DATE, VALID_PRODUCT_ID, VALID_BRAND_ID));
        }
    }

    @Nested
    class InvalidApplicationDate {

        @Test
        void shouldThrowWhenApplicationDateIsNull() {
            assertThatThrownBy(() -> validator.validate(null, VALID_PRODUCT_ID, VALID_BRAND_ID))
                    .isInstanceOf(InvalidPriceException.class)
                    .hasMessageContaining("applicationDate must not be null");
        }
    }

    @Nested
    class InvalidProductId {

        @Test
        void shouldThrowWhenProductIdIsNull() {
            assertThatThrownBy(() -> validator.validate(VALID_DATE, null, VALID_BRAND_ID))
                    .isInstanceOf(InvalidPriceException.class)
                    .hasMessageContaining("productId must be greater than zero");
        }

        @ParameterizedTest(name = "productId={0} should be rejected")
        @ValueSource(longs = {0L, -1L, -100L})
        void shouldThrowWhenProductIdIsZeroOrNegative(long productId) {
            assertThatThrownBy(() -> validator.validate(VALID_DATE, productId, VALID_BRAND_ID))
                    .isInstanceOf(InvalidPriceException.class)
                    .hasMessageContaining("productId must be greater than zero");
        }
    }

    @Nested
    class InvalidBrandId {

        @Test
        void shouldThrowWhenBrandIdIsNull() {
            assertThatThrownBy(() -> validator.validate(VALID_DATE, VALID_PRODUCT_ID, null))
                    .isInstanceOf(InvalidPriceException.class)
                    .hasMessageContaining("brandId must be greater than zero");
        }

        @ParameterizedTest(name = "brandId={0} should be rejected")
        @ValueSource(longs = {0L, -1L, -100L})
        void shouldThrowWhenBrandIdIsZeroOrNegative(long brandId) {
            assertThatThrownBy(() -> validator.validate(VALID_DATE, VALID_PRODUCT_ID, brandId))
                    .isInstanceOf(InvalidPriceException.class)
                    .hasMessageContaining("brandId must be greater than zero");
        }
    }
}
