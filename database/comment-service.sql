-- ============================================================
-- COMMENT-SERVICE - Base de datos PostgreSQL
-- Comentarios por dispositivo (device_id es referencia lógica a device-service)
-- ============================================================

-- Ejecutar contra comment_db: psql -U postgres -d comment_db -f comment-service.sql
-- ------------------------------------------------------------
-- Tabla: comments (Comentarios)
-- device_id referencia al ID del dispositivo en device-service (sin FK)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS comments (
    id         BIGSERIAL PRIMARY KEY,
    device_id  BIGINT NOT NULL,
    author     VARCHAR(100) NOT NULL,
    text       TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_comments_device ON comments(device_id);
CREATE INDEX IF NOT EXISTS idx_comments_created ON comments(created_at DESC);

-- ------------------------------------------------------------
-- DATOS DE PRUEBA - comment-service
-- Asumir dispositivos con id 1, 2, 3 en device-service
-- ------------------------------------------------------------
INSERT INTO comments (device_id, author, text, created_at) VALUES
    (1, 'Usuario1', 'Muy buen sonido y Alexa responde bien.', NOW() - INTERVAL '2 days'),
    (1, 'Ana', 'Fácil de configurar. Lo recomiendo.', NOW() - INTERVAL '1 day'),
    (2, 'Carlos', 'La pantalla es útil para ver recetas.', NOW() - INTERVAL '3 hours'),
    (3, 'María', 'Integración perfecta con Apple.', NOW() - INTERVAL '5 days')
;
