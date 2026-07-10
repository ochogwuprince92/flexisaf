CREATE TABLE IF NOT EXISTS shipments (
    shipment_id        BIGSERIAL       PRIMARY KEY,
    tracking_number    VARCHAR(20)     NOT NULL UNIQUE,
    origin             TEXT            NOT NULL,
    destination        TEXT            NOT NULL,
    status             VARCHAR(30)     NOT NULL DEFAULT 'PENDING',
    weight_kg          NUMERIC(10, 2)  NOT NULL,
    declared_value     NUMERIC(12, 2),
    delivery_notes     TEXT,
    requires_signature BOOLEAN         NOT NULL DEFAULT FALSE,
    created_at         TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMP,
    version            BIGINT          NOT NULL DEFAULT 0
);

CREATE INDEX idx_shipment_status
    ON shipments (status);

CREATE INDEX idx_shipment_tracking
    ON shipments (tracking_number);