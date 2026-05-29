CREATE TABLE customers (
    customer_id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(120) NOT NULL,
    phone VARCHAR(20) UNIQUE,
    email VARCHAR(150) UNIQUE,
    address TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE warehouses (
    warehouse_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    location TEXT NOT NULL
);

CREATE TABLE routes (
    route_id BIGSERIAL PRIMARY KEY,
    origin TEXT NOT NULL,
    destination TEXT NOT NULL,
    estimated_time_min INT NOT NULL
);

CREATE TABLE drivers (
    driver_id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(120) NOT NULL,
    phone VARCHAR(20) UNIQUE,
    license_number VARCHAR(50) UNIQUE,
    status VARCHAR(20) DEFAULT 'AVAILABLE'
);

CREATE TABLE vehicles (
    vehicle_id BIGSERIAL PRIMARY KEY,
    plate_number VARCHAR(50) UNIQUE NOT NULL,
    type VARCHAR(20) NOT NULL,
    capacity_kg DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'AVAILABLE'
);

CREATE TABLE shipments (
    shipment_id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT REFERENCES customers(customer_id),
    origin TEXT NOT NULL,
    destination TEXT NOT NULL,
    status VARCHAR(30) DEFAULT 'CREATED',
    weight_kg DECIMAL(10,2) CHECK (weight_kg > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE parcels (
    parcel_id BIGSERIAL PRIMARY KEY,
    shipment_id BIGINT REFERENCES shipments(shipment_id),
    description TEXT,
    weight DECIMAL(10,2) NOT NULL,
    fragile BOOLEAN DEFAULT FALSE
);

CREATE TABLE tracking_events (
    event_id BIGSERIAL PRIMARY KEY,
    shipment_id BIGINT REFERENCES shipments(shipment_id),
    status VARCHAR(50) NOT NULL,
    location TEXT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    remarks TEXT
);

CREATE TABLE payments (
    payment_id BIGSERIAL PRIMARY KEY,
    shipment_id BIGINT UNIQUE REFERENCES shipments(shipment_id),
    amount DECIMAL(10,2) NOT NULL,
    method VARCHAR(30) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    paid_at TIMESTAMP
);

CREATE TABLE shipment_assignments (
    assignment_id BIGSERIAL PRIMARY KEY,
    shipment_id BIGINT REFERENCES shipments(shipment_id),
    driver_id BIGINT REFERENCES drivers(driver_id),
    vehicle_id BIGINT REFERENCES vehicles(vehicle_id),
    route_id BIGINT REFERENCES routes(route_id),
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);