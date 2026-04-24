package com.ecommerce.domain.model;

import com.ecommerce.domain.exception.InvalidPriceException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

    private static final LocalDateTime START_DATE = LocalDateTime.of(2020, 6, 14, 0, 0);
    private static final LocalDateTime END_DATE = LocalDateTime.of(2020, 12, 31, 23, 59, 59);
    private static final Long BRAND_ID_VALUE = 1L;
    private static final Long PRODUCT_ID_VALUE = 35455L;
    private static final int PRICE_LIST = 1;
    private static final int PRIORITY_ZERO = 0;
    private static final BigDecimal VALID_PRICE = new BigDecimal("35.50");
    private static final String CURRENCY = "EUR";

    private Price buildValidPrice(int priority) {
        return new Price(new BrandId(BRAND_ID_VALUE), START_DATE, END_DATE, PRICE_LIST,
                new ProductId(PRODUCT_ID_VALUE), priority, VALID_PRICE, CURRENCY);
    }

    @Nested
    class ConstructorValidation {

        @Test
        void shouldThrowWhenStartDateIsAfterEndDate() {
            assertThatThrownBy(() -> new Price(
                    new BrandId(BRAND_ID_VALUE), END_DATE, START_DATE, PRICE_LIST,
                    new ProductId(PRODUCT_ID_VALUE), PRIORITY_ZERO, VALID_PRICE, CURRENCY))
                    .isInstanceOf(InvalidPriceException.class)
                    .hasMessageContaining("startDate must be before or equal to endDate");
        }

        @Test
        void shouldThrowWhenPriorityIsNegative() {
            assertThatThrownBy(() -> new Price(
                    new BrandId(BRAND_ID_VALUE), START_DATE, END_DATE, PRICE_LIST,
                    new ProductId(PRODUCT_ID_VALUE), -1, VALID_PRICE, CURRENCY))
                    .isInstanceOf(InvalidPriceException.class)
                    .hasMessageContaining("priority must be >= 0");
        }

        @Test
        void shouldThrowWhenPriceIsNegative() {
            assertThatThrownBy(() -> new Price(
                    new BrandId(BRAND_ID_VALUE), START_DATE, END_DATE, PRICE_LIST,
                    new ProductId(PRODUCT_ID_VALUE), PRIORITY_ZERO, new BigDecimal("-0.01"), CURRENCY))
                    .isInstanceOf(InvalidPriceException.class)
                    .hasMessageContaining("price must be >= 0");
        }

        @Test
        void shouldAcceptEqualStartAndEndDate() {
            Price price = new Price(new BrandId(BRAND_ID_VALUE), START_DATE, START_DATE, PRICE_LIST,
                    new ProductId(PRODUCT_ID_VALUE), PRIORITY_ZERO, VALID_PRICE, CURRENCY);
            assertThat(price).isNotNull();
        }

        @Test
        void shouldAcceptZeroPrice() {
            Price price = new Price(new BrandId(BRAND_ID_VALUE), START_DATE, END_DATE, PRICE_LIST,
                    new ProductId(PRODUCT_ID_VALUE), PRIORITY_ZERO, BigDecimal.ZERO, CURRENCY);
            assertThat(price.price()).isEqualByComparingTo(BigDecimal.ZERO);
        }
    }

    @Nested
    class IsApplicable {

        @Test
        void shouldReturnTrueWhenDateIsInsideRange() {
            Price price = buildValidPrice(PRIORITY_ZERO);

            assertThat(price.isApplicable(LocalDateTime.of(2020, 7, 1, 12, 0))).isTrue();
        }

        @Test
        void shouldReturnFalseWhenDateIsBeforeRange() {
            Price price = buildValidPrice(PRIORITY_ZERO);

            assertThat(price.isApplicable(LocalDateTime.of(2020, 6, 13, 23, 59, 59))).isFalse();
        }

        @Test
        void shouldReturnFalseWhenDateIsAfterRange() {
            Price price = buildValidPrice(PRIORITY_ZERO);

            assertThat(price.isApplicable(LocalDateTime.of(2021, 1, 1, 0, 0, 0))).isFalse();
        }

        @Test
        void shouldReturnTrueWhenDateEqualsStartDate() {
            Price price = buildValidPrice(PRIORITY_ZERO);

            assertThat(price.isApplicable(START_DATE)).isTrue();
        }

        @Test
        void shouldReturnTrueWhenDateEqualsEndDate() {
            Price price = buildValidPrice(PRIORITY_ZERO);

            assertThat(price.isApplicable(END_DATE)).isTrue();
        }

        @ParameterizedTest(name = "date={0} should be applicable")
        @ValueSource(strings = {"2020-06-14T00:00:00", "2020-07-01T12:00:00", "2020-12-31T23:59:59"})
        void shouldReturnTrueForMultipleDateScenariosInsideRange(String isoDate) {
            Price price = buildValidPrice(PRIORITY_ZERO);

            assertThat(price.isApplicable(LocalDateTime.parse(isoDate))).isTrue();
        }
    }

    @Nested
    class IsPromotional {

        @Test
        void shouldReturnTrueWhenPriorityIsGreaterThanZero() {
            Price price = buildValidPrice(1);

            assertThat(price.isPromotional()).isTrue();
        }

        @Test
        void shouldReturnFalseWhenPriorityIsZero() {
            Price price = buildValidPrice(PRIORITY_ZERO);

            assertThat(price.isPromotional()).isFalse();
        }
    }
}
