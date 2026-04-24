package com.ecommerce.infrastructure.web;

import com.ecommerce.domain.model.BrandId;
import com.ecommerce.domain.model.Price;
import com.ecommerce.domain.model.ProductId;
import com.ecommerce.domain.port.in.GetPriceUseCase;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PriceController.class)
class PriceControllerTest {

    private static final Long PRODUCT_ID = 35455L;
    private static final Long BRAND_ID = 1L;
    private static final LocalDateTime APPLICATION_DATE = LocalDateTime.of(2020, 6, 14, 10, 0);
    private static final LocalDateTime START_DATE = LocalDateTime.of(2020, 6, 14, 0, 0);
    private static final LocalDateTime END_DATE = LocalDateTime.of(2020, 12, 31, 23, 59, 59);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetPriceUseCase getPriceUseCase;

    @MockBean
    private PriceMapper priceMapper;

    @Nested
    class SuccessScenarios {

        @Test
        void shouldReturnPriceWhenValidRequest() throws Exception {
            // Arrange
            Price price = new Price(new BrandId(BRAND_ID), START_DATE, END_DATE, 1,
                    new ProductId(PRODUCT_ID), 0, new BigDecimal("35.50"), "EUR");
            PriceResponse response = new PriceResponse(PRODUCT_ID, BRAND_ID, 1, START_DATE, END_DATE,
                    new BigDecimal("35.50"), "EUR");

            when(getPriceUseCase.getPrice(any(), eq(PRODUCT_ID), eq(BRAND_ID)))
                    .thenReturn(Optional.of(price));
            when(priceMapper.toResponse(price)).thenReturn(response);

            // Act & Assert
            mockMvc.perform(get("/api/v1/prices")
                            .param("applicationDate", "2020-06-14T10:00:00")
                            .param("productId", String.valueOf(PRODUCT_ID))
                            .param("brandId", String.valueOf(BRAND_ID))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productId").value(PRODUCT_ID))
                    .andExpect(jsonPath("$.brandId").value(BRAND_ID))
                    .andExpect(jsonPath("$.priceList").value(1))
                    .andExpect(jsonPath("$.price").value(35.50))
                    .andExpect(jsonPath("$.startDate").value("2020-06-14T00:00:00"))
                    .andExpect(jsonPath("$.endDate").value("2020-12-31T23:59:59"))
                    .andExpect(jsonPath("$.currency").value("EUR"));
        }

        @Test
        void shouldReturnPriceWhenApplicationDateEqualsStartDate() throws Exception {
            // Arrange
            Price price = new Price(new BrandId(BRAND_ID), START_DATE, END_DATE, 1,
                    new ProductId(PRODUCT_ID), 0, new BigDecimal("35.50"), "EUR");
            PriceResponse response = new PriceResponse(PRODUCT_ID, BRAND_ID, 1, START_DATE, END_DATE,
                    new BigDecimal("35.50"), "EUR");

            when(getPriceUseCase.getPrice(eq(START_DATE), eq(PRODUCT_ID), eq(BRAND_ID)))
                    .thenReturn(Optional.of(price));
            when(priceMapper.toResponse(price)).thenReturn(response);

            // Act & Assert
            mockMvc.perform(get("/api/v1/prices")
                            .param("applicationDate", "2020-06-14T00:00:00")
                            .param("productId", String.valueOf(PRODUCT_ID))
                            .param("brandId", String.valueOf(BRAND_ID))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productId").value(PRODUCT_ID))
                    .andExpect(jsonPath("$.priceList").value(1));
        }

        @Test
        void shouldReturnPriceWhenApplicationDateEqualsEndDate() throws Exception {
            // Arrange
            Price price = new Price(new BrandId(BRAND_ID), START_DATE, END_DATE, 1,
                    new ProductId(PRODUCT_ID), 0, new BigDecimal("35.50"), "EUR");
            PriceResponse response = new PriceResponse(PRODUCT_ID, BRAND_ID, 1, START_DATE, END_DATE,
                    new BigDecimal("35.50"), "EUR");

            when(getPriceUseCase.getPrice(eq(END_DATE), eq(PRODUCT_ID), eq(BRAND_ID)))
                    .thenReturn(Optional.of(price));
            when(priceMapper.toResponse(price)).thenReturn(response);

            // Act & Assert
            mockMvc.perform(get("/api/v1/prices")
                            .param("applicationDate", "2020-12-31T23:59:59")
                            .param("productId", String.valueOf(PRODUCT_ID))
                            .param("brandId", String.valueOf(BRAND_ID))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productId").value(PRODUCT_ID))
                    .andExpect(jsonPath("$.priceList").value(1));
        }
    }

    @Nested
    class NotFoundScenarios {

        @Test
        void shouldReturn404WhenNoPriceFound() throws Exception {
            // Arrange
            when(getPriceUseCase.getPrice(any(), any(), any())).thenReturn(Optional.empty());

            // Act & Assert
            mockMvc.perform(get("/api/v1/prices")
                            .param("applicationDate", "2020-06-14T10:00:00")
                            .param("productId", "99999")
                            .param("brandId", "99")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class InvalidInputScenarios {

        @Test
        void shouldFailWhenBrandIdIsMissing() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/api/v1/prices")
                            .param("applicationDate", "2020-06-14T10:00:00")
                            .param("productId", "35455")
                            // Missing brandId
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldFailWhenProductIdIsMissing() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/api/v1/prices")
                            .param("applicationDate", "2020-06-14T10:00:00")
                            // Missing productId
                            .param("brandId", "1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldFailWhenApplicationDateIsMissing() throws Exception {
            // Act & Assert
            mockMvc.perform(get("/api/v1/prices")
                            // Missing applicationDate
                            .param("productId", "35455")
                            .param("brandId", "1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }
}
