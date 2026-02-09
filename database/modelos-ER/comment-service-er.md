# Modelo Entidad-Relación: comment-service

Base de datos: **comment_db**

## Entidades

### comments (Comentarios)
| Atributo  | Tipo        | Restricciones | Notas                         |
|-----------|-------------|---------------|-------------------------------|
| id        | BIGSERIAL   | PK            |                               |
| device_id | BIGINT      | NOT NULL      | Ref. lógica a device-service  |
| author    | VARCHAR(100)| NOT NULL      |                               |
| text      | TEXT        | NOT NULL      |                               |
| created_at| TIMESTAMP   | NOT NULL      | DEFAULT CURRENT_TIMESTAMP    |

## Relaciones

- **device_id**: referencia lógica al ID del dispositivo en device-service. No existe FK entre bases de datos (estilo microservicios).

## Diagrama

```
+------------------+
|    comments      |
+------------------+
| id (PK)          |
| device_id        | ---- (ref. lógica device-service)
| author           |
| text             |
| created_at       |
+------------------+
```

Relación lógica: un dispositivo (en device-service) tiene muchos comentarios (en comment-service). La integridad referencial se gestiona a nivel de aplicación.
