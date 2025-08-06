
# Ecommerce Microservices Backend

This is a microservices-based e-commerce application built with Spring Boot, featuring three core services: Wallet, Shop, and Inventory. Each service is independently deployable and interacts via RESTful APIs using Spring Cloud components like Eureka, Feign, API Gateway, and Resilience4j for fault tolerance.

The system is frontend-independent and can be integrated with any client-side technology — including mobile apps (e.g., Flutter, React Native) and web apps (e.g., React, Angular).

Microservice Breakdown

    👜 Wallet Service: Manages user authentication, wallet balance, and financial transactions.

    🛍️ Shop Service: Handles product listings, shopping cart operations, order placement, and payment processing.

    📦 Inventory Service: Maintains inventory data, manages stock levels, and syncs product availability.

Supporting Services

    🧭 Eureka Server: For service discovery across microservices.

    ⚙️ Config Server: Centralized configuration management.

    🌐 API Gateway: Routes and secures requests to the appropriate microservice.

Each microservice includes a standardized API response class to ensure consistent and clean communication between services and with the client.

## 🛡️ Fault Tolerance with Resilience4j

To ensure **reliability** and **stability**, the project integrates **[Resilience4j](https://resilience4j.readme.io/)**, a fault-tolerance library designed for Java microservices. It provides several powerful patterns for handling runtime failures and service outages.

### Key Features

- **Circuit Breakers**  
  Prevent cascading failures by temporarily halting calls to services that are failing. Automatically resets after a cooldown period.

- **Rate Limiters**  
  Restrict the number of calls to a service in a given time window to avoid overwhelming downstream systems.

- **Retry Mechanism**  
  Automatically retry failed requests a specified number of times before giving up.

- **Fallback Methods**  
  Gracefully handle service failures by providing default behavior or responses when the target service is down or unresponsive.

###  Where It's Used

These mechanisms are applied to **inter-service communication via Feign clients**, helping services recover from temporary issues such as:

- Network latency
- Service downtime
- Slow or unresponsive upstream dependencies
- Sudden traffic spikes


## Tech Stack

Java + Spring Boot

Spring Cloud (Eureka, Gateway, Config Server, Feign, Resilience4j)

Spring Data JPA

MySQL

Maven


## Environment Variables

To run this project, you will need to add the following environment variables to your .env file

`DB_PASSWORD`
## Run Locally

Clone the project

```bash
  git clone https://github.com/Renad03/Ecommerce-Microservices-Backend.git
```

Go to the project directory

```bash
  cd Ecommerce_Microservices_Backend
```

Start the services in the correct order

```bash
Using IDE:
1. Run eureka-server
2. Run config-server
3. Run api-gateway
4. Run wallet-service
5. Run shop-service
6. Run inventory-service
```
## 📬 API Testing

You can test the APIs using the following Postman collection:  
🔗 [Click to open in Postman](
https://luxebeauty.postman.co/workspace/Cosmetics-Ecommerce~eae5b93d-b18c-4030-a0d1-8a98214a6de6/collection/38020700-05d54657-a148-4cfd-a617-a85178c743ec?action=share&creator=38020700)



