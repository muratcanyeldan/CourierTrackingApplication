--  courier table
CREATE TABLE IF NOT EXISTS courier
(
    id             UUID PRIMARY KEY,
    total_distance DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    last_lat       DOUBLE PRECISION,
    last_lng       DOUBLE PRECISION,
    create_date    TIMESTAMP                 DEFAULT CURRENT_TIMESTAMP
);

--  store table
CREATE TABLE IF NOT EXISTS store
(
    name VARCHAR(255) PRIMARY KEY,
    lat  DOUBLE PRECISION NOT NULL,
    lng  DOUBLE PRECISION NOT NULL
);

--  courier_store_entry table
CREATE TABLE IF NOT EXISTS courier_store_entry
(
    id          BIGSERIAL PRIMARY KEY,
    courier_id  UUID             NOT NULL,
    store_name  VARCHAR(255)     NOT NULL,
    "timestamp" TIMESTAMP        NOT NULL,
    lat         DOUBLE PRECISION NOT NULL,
    lng         DOUBLE PRECISION NOT NULL,
    CONSTRAINT fk_courier_store_entry_courier
        FOREIGN KEY (courier_id) REFERENCES courier (id),
    CONSTRAINT fk_courier_store_entry_store
        FOREIGN KEY (store_name) REFERENCES store (name)
);


CREATE INDEX IF NOT EXISTS idx_courier_store_entry_courier_id
    ON courier_store_entry (courier_id);

CREATE INDEX IF NOT EXISTS idx_courier_store_entry_store_name
    ON courier_store_entry (store_name);
