# E-commerce Price Query Service

A **Spring Boot** REST service that returns the applicable sale price for a product of a given brand at a specific point in time. When multiple tariffs overlap, the one with the highest priority wins.

---

## Summary

| | |
|---|---|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.2 |
| **Database** | H2 (in-memory) |
| **Architecture** | Hexagonal (Ports & Adapters) |
| **Build tool** | Maven |

### Project structure

```
src/
├── main/java/com/ecommerce/
│   ├── EcommerceApplication.java          # Spring Boot entry point
│   ├── ApplicationConfig.java             # Bean wiring
│   ├── domain/
│   │   ├── model/Price.java               # Domain model
│   │   ├── port/in/GetPriceUseCase.java   # Input port (interface)
│   │   ├── port/out/PriceRepositoryPort.java # Output port (interface)
│   │   └── service/GetPriceService.java   # Domain service
│   └── infrastructure/
│       ├── persistence/
│       │   ├── PriceEntity.java           # JPA entity
│       │   ├── JpaPriceRepository.java    # Spring Data repository
│       │   └── PriceRepositoryAdapter.java # Output port adapter
│       └── web/
│           ├── PriceController.java       # REST controller
│           └── PriceResponse.java         # Response DTO
└── main/resources/
    ├── application.properties
    ├── schema.sql                          # H2 table definition
    └── data.sql                            # Seed data (4 rows)
```

The domain layer has **zero** dependencies on Spring or JPA — all framework coupling lives in the `infrastructure` package.

---

## Prerequisites

- Java 17+
- Maven 3.8+

---

## How to build and run

### Run the application

```bash
mvn spring-boot:run
```

The service starts on **http://localhost:8080**.

### Run tests

```bash
mvn test
```

### Build a JAR

```bash
mvn package
java -jar target/test-ecommerce-0.0.1-SNAPSHOT.jar
```

---

## API

### `GET /api/prices`

Returns the applicable price tariff for the given product, brand, and datetime.

#### Query parameters

| Parameter | Type | Format | Required | Description |
|---|---|---|---|---|
| `applicationDate` | string | ISO-8601 (`yyyy-MM-dd'T'HH:mm:ss`) | ✅ | The date/time to look up |
| `productId` | long | integer | ✅ | Product identifier |
| `brandId` | long | integer | ✅ | Brand identifier (e.g. 1 = ZARA) |

#### Response — `200 OK`

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 2,
  "startDate": "2020-06-14T15:00:00",
  "endDate": "2020-06-14T18:30:00",
  "price": 25.45,
  "currency": "EUR"
}
```

| Field | Description |
|---|---|
| `productId` | Product identifier |
| `brandId` | Brand identifier |
| `priceList` | Tariff/price-list identifier that applies |
| `startDate` | Start of the tariff validity period |
| `endDate` | End of the tariff validity period |
| `price` | Final sale price |
| `currency` | ISO 4217 currency code |

#### Response — `404 Not Found`

Returned when no tariff is active for the given combination of product, brand, and date.

#### Example requests (curl)

```bash
# Test 1 – 10:00 on 14th → price list 1, €35.50
curl "http://localhost:8080/api/prices?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1"

# Test 2 – 16:00 on 14th → price list 2, €25.45
curl "http://localhost:8080/api/prices?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1"

# Test 3 – 21:00 on 14th → price list 1, €35.50
curl "http://localhost:8080/api/prices?applicationDate=2020-06-14T21:00:00&productId=35455&brandId=1"

# Test 4 – 10:00 on 15th → price list 3, €30.50
curl "http://localhost:8080/api/prices?applicationDate=2020-06-15T10:00:00&productId=35455&brandId=1"

# Test 5 – 21:00 on 16th → price list 4, €38.95
curl "http://localhost:8080/api/prices?applicationDate=2020-06-16T21:00:00&productId=35455&brandId=1"
```

---

## Seed data (PRICES table)

| BRAND_ID | START_DATE | END_DATE | PRICE_LIST | PRODUCT_ID | PRIORITY | PRICE | CURR |
|:---|:---|:---|:---|:---|:---|:---|:---|
| 1 | 2020-06-14 00:00:00 | 2020-12-31 23:59:59 | 1 | 35455 | 0 | 35.50 | EUR |
| 1 | 2020-06-14 15:00:00 | 2020-06-14 18:30:00 | 2 | 35455 | 1 | 25.45 | EUR |
| 1 | 2020-06-15 00:00:00 | 2020-06-15 11:00:00 | 3 | 35455 | 1 | 30.50 | EUR |
| 1 | 2020-06-15 16:00:00 | 2020-12-31 23:59:59 | 4 | 35455 | 1 | 38.95 | EUR |

**PRIORITY** disambiguates overlapping tariffs — the highest numeric value wins.

---

## H2 Console

The in-memory H2 console is available at **http://localhost:8080/h2-console** while the application is running.

- **JDBC URL:** `jdbc:h2:mem:ecommercedb`
- **User:** `sa`
- **Password:** *(empty)*

---

## Postman collection

A ready-to-import Postman collection with all 5 test requests is located at:

```
postman/ecommerce-price-service.postman_collection.json
```

Import it in Postman via **File → Import**, or drag-and-drop the file onto the Postman window. The collection targets `http://localhost:8080` — make sure the application is running before sending requests.

---

## Integration tests

Five `@SpringBootTest` integration tests cover every required scenario:

| Test | Date & time | Expected price list | Expected price |
|---|---|---|---|
| Test 1 | 2020-06-14 10:00 | 1 | €35.50 |
| Test 2 | 2020-06-14 16:00 | 2 | €25.45 |
| Test 3 | 2020-06-14 21:00 | 1 | €35.50 |
| Test 4 | 2020-06-15 10:00 | 3 | €30.50 |
| Test 5 | 2020-06-16 21:00 | 4 | €38.95 |

Run them with:

```bash
mvn test
```
