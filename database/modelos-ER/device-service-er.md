# Modelo Entidad-Relación: device-service

Base de datos: **device_db**

## Entidades

### devices (Dispositivos)
| Atributo      | Tipo          | Restricciones | Notas                    |
|---------------|---------------|---------------|--------------------------|
| id            | BIGSERIAL     | PK            |                          |
| name          | VARCHAR(200)  | NOT NULL      |                          |
| description   | TEXT          |               |                          |
| brand_id      | BIGINT        | NOT NULL      | Ref. lógica a catalog   |
| device_type_id| BIGINT        | NOT NULL      | Ref. lógica a catalog   |
| release_date  | DATE          |               |                          |
| image_url     | VARCHAR(500)  |               | Imagen principal         |

### device_images (Imágenes adicionales)
| Atributo | Tipo         | Restricciones | Notas        |
|----------|--------------|---------------|--------------|
| device_id| BIGINT       | FK, NOT NULL  | → devices.id |
| url      | VARCHAR(500) | NOT NULL      |              |

## Relaciones

- **devices** 1 ──< **device_images**: un dispositivo puede tener varias URLs de imagen.
- **brand_id** y **device_type_id** no son FKs físicas; apuntan a entidades en otro microservicio (catalog-service).

## Diagrama

```
                    +------------------+
                    |    devices      |
                    +------------------+
                    | id (PK)         |
                    | name            |
                    | description     |
                    | brand_id        | ---- (ref. lógica catalog)
                    | device_type_id  | ---- (ref. lógica catalog)
                    | release_date    |
                    | image_url       |
                    +--------+--------+
                             |
                             | 1:N
                             v
                    +------------------+
                    | device_images   |
                    +------------------+
                    | device_id (FK)  |
                    | url             |
                    +------------------+
```
