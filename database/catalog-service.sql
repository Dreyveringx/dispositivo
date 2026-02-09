-- ============================================================
-- CATALOG-SERVICE - Base de datos PostgreSQL
-- Catálogo: marcas y tipos de dispositivo (datos maestros)
-- ============================================================

-- Ejecutar contra catalog_db: psql -U postgres -d catalog_db -f catalog-service.sql
-- ------------------------------------------------------------
-- Tabla: brands (Marcas)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS brands (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(500)
);

-- ------------------------------------------------------------
-- Tabla: device_types (Tipos de dispositivo)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS device_types (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(500)
);

-- Índices opcionales para búsquedas por nombre
CREATE INDEX IF NOT EXISTS idx_brands_name ON brands(name);
CREATE INDEX IF NOT EXISTS idx_device_types_name ON device_types(name);

-- ------------------------------------------------------------
-- DATOS DE PRUEBA - catalog-service
-- ------------------------------------------------------------
INSERT INTO brands (name, description) VALUES
    ('Samsung', 'Tecnología y electrónica de consumo'),
    ('Apple', 'Dispositivos y servicios de consumo'),
    ('Xiaomi', 'Dispositivos inteligentes y móviles'),
    ('Google', 'Nest y dispositivos conectados'),
    ('Amazon', 'Echo y Alexa')
;

INSERT INTO device_types (name, description) VALUES
    ('Asistente de voz', 'Altavoces inteligentes con asistente'),
    ('Termostato', 'Control de climatización'),
    ('Bombilla inteligente', 'Iluminación conectada'),
    ('Cerradura', 'Seguridad y acceso'),
    ('Sensor', 'Sensores de movimiento, temperatura, etc.')
;
