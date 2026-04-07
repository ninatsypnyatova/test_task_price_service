# test_ecommerce
Spring Boot application/service that provides a REST query endpoint that:  Accepts as input: Application date, product identifier, and brand identifier.  Returns as output: Product identifier, brand identifier, tariff to apply, application dates, and the final price to be applied.

It looks like you've got a classic technical assessment on your hands! This is a very common Java/Spring Boot challenge used to evaluate clean architecture and problem-solving skills.

Here is the thorough English translation of the evaluation criteria and the technical requirements.

---

## Test Evaluation Criteria

In addition to the requirements mentioned in the prompt, the following points will be considered for the evaluation of this test:

* **Submission via Repository:** Deliver the code through a version control system (e.g., GitHub/GitLab).
* **Hexagonal Architecture:** Implementation of the ports and adapters pattern.
* **Data Extraction Efficiency:** Performance and logic of your database queries.
* **Integration Testing:** As specifically requested in the prompt.
* **Code Clarity:** Readability and maintainability of the written code.
* **GET Endpoint Best Practices:** Proper use of HTTP methods, status codes, and naming conventions.
* **H2 Database Initialization:** The application must boot up with the provided example data.
* **README File:** Documentation explaining how to run and test the project.
* **SOLID Principles:** Adherence to object-oriented design principles.
* **REST API:** Proper design of the web service.
* **Version Control:** Meaningful commit history and branch management.
* **Response Handling:** The endpoint must return a single, unique result (based on priority).
* **Input Parameters:** Application date, brand ID, and product ID.
* **Output Schema:** Product ID, brand ID, price list/tariff to apply, dates, and final price.

---

## The Technical Challenge

In the company's e-commerce database, we have a **PRICES** table that reflects the final price (RRP) and the specific tariff applied to a product from a specific brand between certain dates. Below is an example of the table with the relevant fields:

### Table: PRICES

| BRAND_ID | START_DATE | END_DATE | PRICE_LIST | PRODUCT_ID | PRIORITY | PRICE | CURR |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | 2020-06-14-00.00.00 | 2020-12-31-23.59.59 | 1 | 35455 | 0 | 35.50 | EUR |
| 1 | 2020-06-14-15.00.00 | 2020-06-14-18.30.00 | 2 | 35455 | 1 | 25.45 | EUR |
| 1 | 2020-06-15-00.00.00 | 2020-06-15-11.00.00 | 3 | 35455 | 1 | 30.50 | EUR |
| 1 | 2020-06-15-16.00.00 | 2020-12-31-23.59.59 | 4 | 35455 | 1 | 38.95 | EUR |

### Field Definitions:
* **BRAND_ID:** Foreign key for the group brand (e.g., 1 = ZARA).
* **START_DATE, END_DATE:** The date range during which the indicated price tariff applies.
* **PRICE_LIST:** Identifier for the applicable price tariff.
* **PRODUCT_ID:** Product code identifier.
* **PRIORITY:** Price application disambiguator. If two tariffs overlap in a date range, the one with the higher priority (highest numerical value) is applied.
* **PRICE:** Final sale price.
* **CURR:** ISO currency code.

---

## Requirements

Build a **Spring Boot** application/service that provides a REST query endpoint that:

1.  **Accepts as input:** Application date, product identifier, and brand identifier.
2.  **Returns as output:** Product identifier, brand identifier, tariff to apply, application dates, and the final price to be applied.

### Technical Implementation:
* Use an **in-memory database (H2 type)** and initialize it with the example data provided above. (You may rename fields or add new ones if necessary; choose data types you deem appropriate).

### Required Automated Tests:
Develop tests for the REST endpoint to validate the following scenarios using the example data:

* **Test 1:** Request at 10:00 on the 14th for product 35455 and brand 1 (ZARA).
* **Test 2:** Request at 16:00 on the 14th for product 35455 and brand 1 (ZARA).
* **Test 3:** Request at 21:00 on the 14th for product 35455 and brand 1 (ZARA).
* **Test 4:** Request at 10:00 on the 15th for product 35455 and brand 1 (ZARA).
* **Test 5:** Request at 21:00 on the 16th for product 35455 and brand 1 (ZARA).

---

## What will be valued:
* The design and construction of the service.
* Code quality.
* Correct results in the requested tests.
