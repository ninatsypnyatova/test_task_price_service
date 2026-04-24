package com.ecommerce.infrastructure.web;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PriceControllerIntegrationTest {

    private static final String PRODUCT_ID = "35455";
    private static final String BRAND_ID = "1";
    private static final String UNKNOWN_PRODUCT_ID = "99999";
    private static final String UNKNOWN_BRAND_ID = "99";

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class PriorityResolutionScenarios {

        /**
         * Test 1: Request at 10:00 on the 14th for product 35455 and brand 1.
         * Expected: price list 1, price 35.50 EUR (only tariff 1 is active).
         */
        @Test
        void test1_requestAt10hOn14th() throws Exception {
            mockMvc.perform(get("/api/v1/prices")
                            .param("applicationDate", "2020-06-14T10:00:00")
                            .param("productId", PRODUCT_ID)
                            .param("brandId", BRAND_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productId").value(35455))
                    .andExpect(jsonPath("$.brandId").value(1))
                    .andExpect(jsonPath("$.priceList").value(1))
                    .andExpect(jsonPath("$.price").value(35.50))
                    .andExpect(jsonPath("$.currency").value("EUR"));
        }

        /**
         * Test 2: Request at 16:00 on the 14th for product 35455 and brand 1.
         * Expected: price list 2, price 25.45 EUR (tariff 2 has higher priority than tariff 1).
         */
        @Test
        void test2_requestAt16hOn14th() throws Exception {
            mockMvc.perform(get("/api/v1/prices")
                            .param("applicationDate", "2020-06-14T16:00:00")
                            .param("productId", PRODUCT_ID)
                            .param("brandId", BRAND_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productId").value(35455))
                    .andExpect(jsonPath("$.brandId").value(1))
                    .andExpect(jsonPath("$.priceList").value(2))
                    .andExpect(jsonPath("$.price").value(25.45))
                    .andExpect(jsonPath("$.currency").value("EUR"));
        }

        /**
         * Test 3: Request at 21:00 on the 14th for product 35455 and brand 1.
         * Expected: price list 1, price 35.50 EUR (only tariff 1 is active after 18:30).
         */
        @Test
        void test3_requestAt21hOn14th() throws Exception {
            mockMvc.perform(get("/api/v1/prices")
                            .param("applicationDate", "2020-06-14T21:00:00")
                            .param("productId", PRODUCT_ID)
                            .param("brandId", BRAND_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productId").value(35455))
                    .andExpect(jsonPath("$.brandId").value(1))
                    .andExpect(jsonPath("$.priceList").value(1))
                    .andExpect(jsonPath("$.price").value(35.50))
                    .andExpect(jsonPath("$.currency").value("EUR"));
        }

        /**
         * Test 4: Request at 10:00 on the 15th for product 35455 and brand 1.
         * Expected: price list 3, price 30.50 EUR (tariff 3 has higher priority than tariff 1).
         */
        @Test
        void test4_requestAt10hOn15th() throws Exception {
            mockMvc.perform(get("/api/v1/prices")
                            .param("applicationDate", "2020-06-15T10:00:00")
                            .param("productId", PRODUCT_ID)
                            .param("brandId", BRAND_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productId").value(35455))
                    .andExpect(jsonPath("$.brandId").value(1))
                    .andExpect(jsonPath("$.priceList").value(3))
                    .andExpect(jsonPath("$.price").value(30.50))
                    .andExpect(jsonPath("$.currency").value("EUR"));
        }

        /**
         * Test 5: Request at 21:00 on the 16th for product 35455 and brand 1.
         * Expected: price list 4, price 38.95 EUR (tariff 4 has higher priority than tariff 1).
         */
        @Test
        void test5_requestAt21hOn16th() throws Exception {
            mockMvc.perform(get("/api/v1/prices")
                            .param("applicationDate", "2020-06-16T21:00:00")
                            .param("productId", PRODUCT_ID)
                            .param("brandId", BRAND_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productId").value(35455))
                    .andExpect(jsonPath("$.brandId").value(1))
                    .andExpect(jsonPath("$.priceList").value(4))
                    .andExpect(jsonPath("$.price").value(38.95))
                    .andExpect(jsonPath("$.currency").value("EUR"));
        }
    }

    @Nested
    class EdgeCaseScenarios {

        /**
         * applicationDate exactly equals the startDate of tariff 1 (2020-06-14T00:00:00).
         * Tariff 1 should be active at its own start boundary.
         * Expected: price list 1, price 35.50 EUR.
         */
        @Test
        void shouldReturnPriceWhenApplicationDateEqualsStartDateBoundary() throws Exception {
            mockMvc.perform(get("/api/v1/prices")
                            .param("applicationDate", "2020-06-14T00:00:00")
                            .param("productId", PRODUCT_ID)
                            .param("brandId", BRAND_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productId").value(35455))
                    .andExpect(jsonPath("$.brandId").value(1))
                    .andExpect(jsonPath("$.priceList").value(1))
                    .andExpect(jsonPath("$.price").value(35.50))
                    .andExpect(jsonPath("$.currency").value("EUR"))
                    .andExpect(jsonPath("$.startDate").value("2020-06-14T00:00:00"))
                    .andExpect(jsonPath("$.endDate").value("2020-12-31T23:59:59"));
        }

        /**
         * applicationDate exactly equals the endDate of tariff 1 (2020-12-31T23:59:59).
         * Tariff 4 (priority 1) also covers this date and has higher priority than tariff 1.
         * Expected: price list 4, price 38.95 EUR (tariff 4 wins).
         */
        @Test
        void shouldReturnPriceWhenApplicationDateEqualsEndDateBoundary() throws Exception {
            mockMvc.perform(get("/api/v1/prices")
                            .param("applicationDate", "2020-12-31T23:59:59")
                            .param("productId", PRODUCT_ID)
                            .param("brandId", BRAND_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productId").value(35455))
                    .andExpect(jsonPath("$.brandId").value(1))
                    .andExpect(jsonPath("$.priceList").value(4))
                    .andExpect(jsonPath("$.price").value(38.95))
                    .andExpect(jsonPath("$.currency").value("EUR"))
                    .andExpect(jsonPath("$.startDate").value("2020-06-15T16:00:00"))
                    .andExpect(jsonPath("$.endDate").value("2020-12-31T23:59:59"));
        }
    }

    @Nested
    class NotFoundScenarios {

        /**
         * Test: Request for a product/brand combination that does not exist.
         * Expected: 404 Not Found.
         */
        @Test
        void shouldReturn404WhenNoPriceFound() throws Exception {
            mockMvc.perform(get("/api/v1/prices")
                            .param("applicationDate", "2020-06-14T10:00:00")
                            .param("productId", UNKNOWN_PRODUCT_ID)
                            .param("brandId", UNKNOWN_BRAND_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }
}
