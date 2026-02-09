-- ============================================================
-- DEVICE-SERVICE - Base de datos PostgreSQL
-- Dispositivos inteligentes (referencias lógicas a catalog_db)
-- ============================================================

-- Ejecutar contra device_db: psql -U postgres -d device_db -f device-service.sql
-- ------------------------------------------------------------
-- Tabla: devices (Dispositivos)
-- brand_id y device_type_id son referencias lógicas al catalog-service
-- (no hay FK entre bases de datos en microservicios)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS devices (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(200) NOT NULL,
    description     TEXT,
    brand_id        BIGINT NOT NULL,
    device_type_id  BIGINT NOT NULL,
    release_date    DATE,
    image_url       VARCHAR(500)
);

-- Tabla para imágenes adicionales (relación 1:N)
CREATE TABLE IF NOT EXISTS device_images (
    device_id BIGINT NOT NULL REFERENCES devices(id) ON DELETE CASCADE,
    url       VARCHAR(500) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_devices_brand ON devices(brand_id);
CREATE INDEX IF NOT EXISTS idx_devices_type ON devices(device_type_id);
CREATE INDEX IF NOT EXISTS idx_devices_release ON devices(release_date);
CREATE INDEX IF NOT EXISTS idx_devices_name ON devices(name);

-- ------------------------------------------------------------
-- DATOS DE PRUEBA - device-service
-- Asumir que en catalog_db: brands 1=Samsung, 2=Apple, 3=Xiaomi, 4=Google, 5=Amazon
-- device_types: 1=Asistente, 2=Termostato, 3=Bombilla, 4=Cerradura, 5=Sensor
-- ------------------------------------------------------------
INSERT INTO devices (name, description, brand_id, device_type_id, release_date, image_url) VALUES
    ('Echo Dot (4ª Gen)', 'Altavoz inteligente con Alexa', 5, 1, '2020-10-22', 'https://via.placeholder.com/400x300?text=Echo+Dot'),
    ('Nest Hub', 'Pantalla inteligente de Google', 4, 1, '2021-03-30', 'https://via.placeholder.com/400x300?text=Nest+Hub'),
    ('HomePod mini', 'Altavoz inteligente Apple', 2, 1, '2020-11-16', 'https://via.placeholder.com/400x300?text=HomePod'),
    ('Xiaomi Smart Bulb', 'Bombilla LED WiFi', 3, 3, '2022-01-15', 'https://via.placeholder.com/400x300?text=Smart+Bulb'),
    ('Samsung SmartThings Hub', 'Concentrador para hogar inteligente', 1, 5, '2021-06-01', 'https://via.placeholder.com/400x300?text=SmartThings')
;
