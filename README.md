# Courier Tracking Application

A Spring Boot-based RESTful application designed to track courier locations, calculate travel distances, and automatically log entries when couriers enter a 100m radius of specific Migros stores.

## 🛠 Technology Stack

*   **Java 25**
*   **Spring Boot 3.5.8**
*   **PostgreSQL**
*   **Redis**
*   **Flyway**
*   **Docker & Docker Compose**
*   **OpenAPI / Swagger**

## 📋 Prerequisites

*   **Docker** installed on your machine.

## 🏃 How to Run

The easiest way to run the application and its dependencies (Postgres, Redis) is via Docker Compose.

1.  Clone the repository

```bash
git clone https://github.com/muratcanyeldan/CourierTrackingApplication
```
2. Navigate to the project folder

```bash
cd CourierTrackingApplication
```
3.  Run the following command:

```bash
docker compose up -d --build
```

The application will start on port **9050**.

## 📚 API Documentation

Once the application is running, you can access the interactive API documentation (Swagger UI) at:

👉 **[http://localhost:9050/api/swagger-ui/swagger-ui/index.html](http://localhost:9050/api/swagger-ui/swagger-ui/index.html)**

### Main Endpoints

*   **POST** `/v1/courier/location`: Send a location update.
    ```json
    {
      "time": "2025-11-30T15:39:57.565Z",
      "courier": "3fa85f64-5717-4592-b3fc-2c963f66afa6",
      "lat": 40.9923307,
      "lng": 29.1244229
    }
    ```
*   **GET** `/v1/courier/total-distance/{id}`: Get total travel distance for a courier.

## 🏗 Architecture & Design Patterns

The project follows a layered architecture (Controller -> Service -> Repository) and implements several design patterns:

*   **Strategy Pattern**: Used for distance calculation algorithms (`DistanceStrategy`).
*   **Singleton Pattern**: Spring Beans (Services, Controllers).
*   **Repository Pattern**: Data access abstraction.
*   **Builder Pattern**: Object construction.
