-- ============================================================
-- Creación de bases de datos (ejecutar como superusuario)
-- Ejemplo: psql -U postgres -f scripts-creacion-bases.sql
-- ============================================================

CREATE DATABASE catalog_db;
CREATE DATABASE device_db;
CREATE DATABASE comment_db;

-- Luego ejecutar cada script de servicio en su base:
-- psql -U postgres -d catalog_db -f catalog-service.sql
-- psql -U postgres -d device_db -f device-service.sql
-- psql -U postgres -d comment_db -f comment-service.sql
