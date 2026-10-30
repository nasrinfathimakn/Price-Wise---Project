# PriceWise – Smart Online Price Comparison Platform

**PriceWise** is a full-stack, production-minded web application engineered to compare prices of identical products across major e-commerce retailers (such as Amazon India, Flipkart, Croma, and Reliance Digital). PriceWise guarantees fair comparisons through strict variant disambiguation (e.g., distinguishing 128 GB vs 256 GB models), delivery cost transparency, and real-time freshness tracking.

---

## 1. Architecture Overview

PriceWise is architected with a decoupled Java Spring Boot REST backend and a modern React + Vite frontend.

```
+---------------------------------------------------------------------------------+
|                               React Frontend                                    |
|   (Vite, Tailwind CSS, Lucide React, Recharts, React Router, Auth Context)      |
+---------------------------------------------------------------------------------+
                                      |
                           REST APIs (JSON / JWT)
                                      |
+---------------------------------------------------------------------------------+
|                             Spring Boot 3 Backend                               |
|                                                                                 |
|  [Controller Layer]        -> [Service Layer]             -> [Repository Layer] |
|   - ProductController          - PriceComparisonService        - 11 Spring Data |
|   - PriceComparisonController  - ProductMatchingService          JPA Interfaces |
|   - PriceHistoryController     - RetailerSyncService                            |
|   - AuthController             - PriceAlertService                              |
|   - WatchlistController        - SearchService                                  |
|   - AdminController            - EmailNotificationService                       |
|                                                                                 |
|  [Integration Connectors]      [Security & Utilities]       [Flyway Migrations] |
|   - Amazon PA-API 5.0 Adapter   - Spring Security + BCrypt   - V1 Initial Schema|
|   - Flipkart Affiliate Adapter  - JWT Authentication Filter  - V2 Seed Demo Data|
|   - MockRetailerClient          - Strict Variant Normalizer                     |
+---------------------------------------------------------------------------------+
                                      |
                              PostgreSQL / H2
```

### Directory Structure

```
pricewise/
├── docker-compose.yml              # Local PostgreSQL container service
├── .env.example                    # Environment variables template
├── README.md                       # Comprehensive system documentation
│
├── backend/                        # Java Spring Boot 3 Backend
│   ├── pom.xml                     # Maven project descriptor
│   └── src/
│       ├── main/
│       │   ├── java/com/pricewise/
│       │   │   ├── PriceWiseApplication.java
│       │   │   ├── config/         # Security, OpenAPI, Retailer properties
│       │   │   ├── controller/     # 8 REST API Controllers
│       │   │   ├── dto/            # Strongly-typed Request/Response DTOs
│       │   │   ├── entity/         # 11 JPA Entities
│       │   │   ├── enums/          # Role, AvailabilityStatus, PriceUpdateStatus, etc.
│       │   │   ├── exception/      # GlobalExceptionHandler & custom exceptions
│       │   │   ├── integration/    # RetailerClient, Amazon, Flipkart, Mock clients
│       │   │   ├── repository/     # 11 Spring Data JPA Repositories
│       │   │   ├── scheduler/      # RetailerSyncScheduler & PriceAlertScheduler
│       │   │   ├── security/       # JWT Token Provider & Auth Filters
│       │   │   ├── service/        # 12 Core Business & Comparison Services
│       │   │   └── util/           # PriceUtils, ProductNormalizer, Fuzzy Matcher
│       │   └── resources/
│       │       ├── application.yml
│       │       ├── application-local.yml
│       │       ├── application-postgres.yml
│       │       └── db/migration/
│       │           ├── V1__initial_schema.sql
│       │           └── V2__seed_demo_data.sql
│       └── test/                   # Comprehensive JUnit 5 & Mockito test suite
│
└── frontend/                       # React 18 + Vite Frontend
    ├── package.json
    ├── vite.config.js
    ├── tailwind.config.js
    ├── index.html
    └── src/
        ├── main.jsx
        ├── App.jsx
        ├── api/client.js           # REST API client with Bearer Token interceptor
        ├── context/AuthContext.jsx # JWT session & user role state management
        ├── components/             # 25 Reusable UI Components
        └── pages/                  # 10 Pages (Home, Search, Details, Compare, Admin, etc.)
```

---

## 2. Key Features & Business Logic

### A. Variant Isolation (Anti-Misleading Guarantee)
The engine strictly prevents merging mismatched models or storage/RAM variants.
- For example, a **Samsung Galaxy S25 128 GB** listing will **never** be merged with a **256 GB** canonical product.
- Product matching follows a 5-tier evaluation priority:
  1. **Exact Global Product Identifier**: Match GTIN, EAN, or UPC.
  2. **Manufacturer & Model Number**: Match brand ID + exact model code (`SM-S931B`, `A3101`, etc.).
  3. **Normalized Attribute Comparison**: Brand + extracted storage capacity + RAM configuration.
  4. **Fuzzy Text Similarity**: Jaccard/Levenshtein matching with threshold $\ge 0.85$.
  5. **Disambiguation Flagging**: Matches scored between $0.65$ and $0.85$ are flagged for manual Administrator review via the Admin Console.

### B. Transparent Delivered Price Calculation
Retailers frequently hide high shipping costs behind a lower base price.
- If delivery cost is known: $\text{Total Delivered} = \text{Product Price} + \text{Delivery Charge}$.
- If delivery cost is unknown: The total delivered price is explicitly labeled **Unknown** rather than falsely assuming zero.
- The platform never claims a product is the "cheapest overall" if key retailer offers have missing delivery charges or stale prices.

### C. Discount Percentage Calculation
Discounts are calculated using `BigDecimal` with 2 decimal places and `HALF_UP` rounding:
$$\text{Discount \%} = \frac{\text{Original Price} - \text{Current Price}}{\text{Original Price}} \times 100$$
If original price is missing, non-positive, or lower than the current price, no false discount is reported.

### D. Data Freshness Tracking
Each offer retains its `lastSuccessfulFetchAt` timestamp.
- Freshness threshold is configurable (default: 12 hours).
- If an update fails, historical data is retained while the live offer is clearly flagged as **Stale / Pending Verification**.

---

## 3. Technology Stack

- **Backend**:
  - Java 21 LTS
  - Spring Boot 3.3.4 (Spring Web, Spring Data JPA, Spring Security, Validation)
  - Flyway for database versioning and automatic schema migrations
  - PostgreSQL 16 (Primary) & H2 (Zero-setup local development profile)
  - JJWT (JSON Web Token) for stateless authentication
  - SpringDoc OpenAPI 3 / Swagger UI
  - JUnit 5 & Mockito
- **Frontend**:
  - React 18 with Vite
  - Tailwind CSS with responsive desktop, tablet, and mobile layouts
  - Lucide React icon library
  - Recharts for interactive price history timelines
  - React Router DOM v6

---

## 4. Setup and Execution Guide

### Prerequisites
- Java 21 JDK (`java -version`)
- Apache Maven (`mvn -version`)
- Node.js 18+ and npm (`node -v`, `npm -v`)
- Docker & Docker Compose *(optional for running local PostgreSQL)*

### Step 1: Database Setup

#### Option A: Zero-Config Local Mode (H2 in PostgreSQL-compatibility mode)
By default, the backend runs in `local` profile with in-memory H2 database configured in PostgreSQL-compatibility mode. Flyway automatically creates all tables and seeds realistic demo data on startup. **No database installation required.**

#### Option B: PostgreSQL with Docker Compose
If you prefer running a dedicated PostgreSQL 16 instance:
```bash
# In the project root:
docker-compose up -d
```
Then start the backend with `SPRING_PROFILES_ACTIVE=postgres`.

---

### Step 2: Start the Java Spring Boot Backend

```bash
cd backend

# Run the test suite:
mvn test

# Run the Spring Boot application:
mvn spring-boot:run
```
The backend starts on `http://localhost:8080`.
- **Swagger / OpenAPI Documentation**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Schema**: `http://localhost:8080/api-docs`
- **H2 Console** *(local profile)*: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:pricewise`, User: `sa`, Password: *(empty)*)

---

### Step 3: Start the React Frontend

In a separate terminal:
```bash
cd frontend

# Install dependencies:
npm install

# Start Vite development server:
npm run dev
```
Open your browser to `http://localhost:5173`.

---

## 5. Seed Demonstration Accounts & Data

The application comes pre-loaded with demonstration data:

### Pre-Configured Accounts
| Role | Email | Password |
|---|---|---|
| **Admin** | `admin@pricewise.local` | `Password@123` |
| **User** | `john.doe@example.com` | `Password@123` |

### Pre-Configured Products
1. **Samsung Galaxy S25 5G (256 GB, Onyx Black)** (Model: `SM-S931B`, GTIN: `8806095000012`)
2. **Samsung Galaxy S25 5G (512 GB, Cobalt Violet)** (Model: `SM-S931B-512`, GTIN: `8806095000029`)
3. **Apple iPhone 17 (256 GB, Titanium White)** (Model: `A3101`, GTIN: `1959490000014`)
4. **HP Pavilion 15 Laptop (Intel Core i5, 16 GB, 512 GB SSD)** (Model: `15-eg2000`)
5. **Sony WH-1000XM5 Wireless Headphones** (Model: `WH1000XM5B`)
6. **boAt Stone 650 10W Bluetooth Speaker** (Model: `STONE650`)

---

## 6. External Retailer Integrations & Credentials

The integration architecture implements the Adapter Pattern via `RetailerClient`:
- `AmazonRetailerClient`: Configured for Amazon Product Advertising API (PA-API 5.0).
- `FlipkartRetailerClient`: Configured for Flipkart Affiliate API.
- `MockRetailerClient`: Built-in local mock provider that operates out-of-the-box without external API keys.

### Configuring Live Retailer Credentials
To enable live retailer API connectivity, set the following environment variables (or update `.env`):

```bash
# Amazon PA-API 5.0
AMAZON_INTEGRATION_ENABLED=true
AMAZON_API_KEY=your_access_key
AMAZON_API_SECRET=your_secret_key
AMAZON_ASSOCIATE_TAG=your_store_id-21

# Flipkart Affiliate API
FLIPKART_INTEGRATION_ENABLED=true
FLIPKART_AFFILIATE_ID=your_affiliate_id
FLIPKART_AFFILIATE_TOKEN=your_affiliate_token
```
*Note: If credentials are not configured, the system gracefully operates using the built-in verified demonstration data and will never expose secrets or crash.*

---

## 7. REST API Reference

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `GET` | `/api/v1/products` | Search and filter products | Public |
| `GET` | `/api/v1/products/{id}` | Canonical product details | Public |
| `GET` | `/api/v1/products/{id}/comparison` | Compare retailer offers with lowest price | Public |
| `GET` | `/api/v1/products/{id}/price-history` | Historical observations & chart series | Public |
| `GET` | `/api/v1/categories` | List categories | Public |
| `GET` | `/api/v1/search/suggestions?q={q}` | Autocomplete suggestions | Public |
| `POST` | `/api/v1/auth/register` | Register new user | Public |
| `POST` | `/api/v1/auth/login` | Authenticate & obtain JWT | Public |
| `GET` | `/api/v1/auth/me` | Current user profile | Authenticated |
| `GET` | `/api/v1/watchlist` | Get personal watchlist | Authenticated |
| `POST` | `/api/v1/watchlist` | Add product to watchlist | Authenticated |
| `DELETE` | `/api/v1/watchlist/{id}` | Remove product from watchlist | Authenticated |
| `GET` | `/api/v1/price-alerts` | List target-price alerts | Authenticated |
| `POST` | `/api/v1/price-alerts` | Create target price alert | Authenticated |
| `PATCH` | `/api/v1/price-alerts/{id}` | Toggle alert status | Authenticated |
| `DELETE` | `/api/v1/price-alerts/{id}` | Delete alert | Authenticated |
| `GET` | `/api/v1/admin/dashboard` | Admin operational metrics | Admin Only |
| `POST` | `/api/v1/admin/retailers/{id}/sync`| Trigger manual retailer sync | Admin Only |
| `GET` | `/api/v1/admin/update-logs` | Review price sync logs | Admin Only |
| `GET` | `/api/v1/admin/product-matches/review` | Review ambiguous matches | Admin Only |
| `POST` | `/api/v1/admin/product-matches/{id}/resolve` | Approve / reject match | Admin Only |

