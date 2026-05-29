# FlexiSAF — Spring Boot REST API

A production-ready RESTful API built with Java 21 and Spring Boot, backed by PostgreSQL and managed with Maven. This project demonstrates clean backend architecture following industry best practices including layered design, input validation, structured error handling and database integration.

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 3.4 |
| Database | PostgreSQL 15 |
| ORM | Spring Data JPA / Hibernate |
| Build Tool | Maven 3.9 |
| Application Server | Embedded Tomcat (Spring Boot) |
| Validation | Jakarta Bean Validation |
| Code Reduction | Lombok |


# ERD

Logistics Database Schema Design (High-Level)

A logistics system typically manages shipments, deliveries, tracking, vehicles, drivers, warehouses, and customers. Below is a clean relational schema that models how goods move from sender to receiver.

The system supports:

Shipment creation and tracking
Pickup and delivery operations
Warehouse storage management
Vehicle and driver assignment
Real-time shipment status updates
Customer order delivery lifecycle

![ERD](docs/ERD.png)

## RELATIONSHIPS
    CUSTOMERS ||--o{ SHIPMENTS : places

    SHIPMENTS ||--o{ PARCELS : contains
    SHIPMENTS ||--o{ TRACKING_EVENTS : has
    SHIPMENTS ||--|| PAYMENTS : paid_by
    SHIPMENTS ||--|| SHIPMENT_ASSIGNMENTS : assigned_to

    DRIVERS ||--o{ SHIPMENT_ASSIGNMENTS : drives
    VEHICLES ||--o{ SHIPMENT_ASSIGNMENTS : uses
    ROUTES ||--o{ SHIPMENT_ASSIGNMENTS : follows

## ENTITIES
    CUSTOMERS {
        bigint customer_id PK
        string full_name
        string phone
        string email
        text address
        timestamp created_at
    }

    SHIPMENTS {
        bigint shipment_id PK
        bigint customer_id FK
        text origin
        text destination
        string status
        decimal weight_kg
        timestamp created_at
    }

    PARCELS {
        bigint parcel_id PK
        bigint shipment_id FK
        text description
        decimal weight
        boolean fragile
    }

    TRACKING_EVENTS {
        bigint event_id PK
        bigint shipment_id FK
        string status
        text location
        timestamp timestamp
    }

    PAYMENTS {
        bigint payment_id PK
        bigint shipment_id FK
        decimal amount
        string method
        string status
    }

    SHIPMENT_ASSIGNMENTS {
        bigint assignment_id PK
        bigint shipment_id FK
        bigint driver_id FK
        bigint vehicle_id FK
        bigint route_id FK
    }

    DRIVERS {
        bigint driver_id PK
        string full_name
        string phone
        string license_number
        string status
    }

    VEHICLES {
        bigint vehicle_id PK
        string plate_number
        string type
        decimal capacity_kg
        string status
    }

    ROUTES {
        bigint route_id PK
        text origin
        text destination
        int estimated_time_min
    }


## Author

**Ochogwu Prince**
GitHub: [@ochogwuprince92](https://github.com/ochogwuprince92)