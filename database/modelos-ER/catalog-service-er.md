# Modelo Entidad-Relación: catalog-service

Base de datos: **catalog_db**

## Entidades

### brands (Marcas)
| Atributo   | Tipo         | Restricciones |
|-----------|--------------|---------------|
| id        | BIGSERIAL    | PK            |
| name      | VARCHAR(100) | NOT NULL      |
| description | VARCHAR(500) |               |

### device_types (Tipos de dispositivo)
| Atributo   | Tipo         | Restricciones |
|-----------|--------------|---------------|
| id        | BIGSERIAL    | PK            |
| name      | VARCHAR(100) | NOT NULL      |
| description | VARCHAR(500) |               |

## Diagrama

```
+-------------+     +------------------+
|   brands    |     |  device_types    |
+-------------+     +------------------+
| id (PK)     |     | id (PK)          |
| name        |     | name             |
| description |     | description     |
+-------------+     +------------------+
```

No hay relación entre sí; son catálogos independientes usados por device-service vía API.
