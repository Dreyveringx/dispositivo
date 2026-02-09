# Dispositivos Inteligentes - Proyecto Académico

Sitio web para gestión y consulta de **dispositivos inteligentes**, desarrollado con arquitectura de **microservicios** y **arquitectura hexagonal**, front-end en **Angular** y back-end en **Spring Boot**.

---

## Descripción del proyecto

El sistema permite:

- **Consultar** un catálogo de dispositivos inteligentes (asistentes de voz, termostatos, bombillas, etc.) con filtros por marca, tipo y búsqueda por nombre, y ordenamiento por fecha de lanzamiento.
- **Ver el detalle** de cada dispositivo (descripción, imágenes, reseña) y **leer y publicar comentarios**.
- **Administrar** (CRUD) dispositivos, marcas y tipos de dispositivo desde un panel de administración.

La comunicación es **REST** entre el front-end y cada microservicio. Cada microservicio tiene su **propia base de datos PostgreSQL** y aplica **arquitectura hexagonal** (puertos y adaptadores) con dominio independiente del framework.

---

## Justificación de microservicios

Se eligió un estilo **microservicios** por:

1. **Separación de responsabilidades**:  
   - **device-service**: dominio de dispositivos (creación, listado, filtros, detalle).  
   - **comment-service**: solo comentarios por dispositivo.  
   - **catalog-service**: datos maestros (marcas y tipos) usados por el resto.

2. **Escalabilidad independiente**: se puede escalar solo el servicio de comentarios si crece la carga, sin tocar dispositivos ni catálogo.

3. **Tecnología y despliegue**: cada servicio puede evolucionar (versiones de Java, librerías) y desplegarse por separado.

4. **Base de datos por servicio**: cada microservicio tiene su propia BD (device_db, comment_db, catalog_db), evitando acoplamiento y permitiendo modelos de datos adecuados a su contexto.

5. **Fines académicos**: el proyecto queda claro para entender límites de servicios, APIs REST y relaciones lógicas entre dominios (p. ej. device_id en comentarios como referencia al device-service).

---

## Justificación de arquitectura hexagonal

Se aplica **arquitectura hexagonal (Ports & Adapters)** en cada microservicio para:

1. **Dominio en el centro**: la lógica de negocio y los modelos (Device, Comment, Brand, DeviceType) son POJOs sin anotaciones JPA ni dependencias de Spring. El dominio no conoce HTTP ni bases de datos.

2. **Puertos de entrada (in)**: interfaces que definen los casos de uso (p. ej. `DeviceUseCases`, `CommentUseCases`, `BrandUseCases`). El adaptador REST las implementa llamando a estos puertos.

3. **Puertos de salida (out)**: interfaces de persistencia (p. ej. `DeviceRepositoryPort`). Los adaptadores de infraestructura (JPA) implementan estos puertos y traducen entre entidades de dominio y entidades JPA.

4. **Testabilidad**: se pueden probar los casos de uso con repositorios en memoria, sin levantar HTTP ni BD.

5. **Sustitución de adaptadores**: se puede cambiar la API (REST por GraphQL) o la persistencia (JPA por otro almacén) sin tocar el dominio.

En el código **no hay controllers en el dominio** ni **entidades JPA en el dominio**: los controladores y las entidades JPA están solo en la capa de infraestructura (adaptadores).

---

## Descripción de cada microservicio

### 1. device-service (puerto 8080)

- **Responsabilidad**: gestión de dispositivos inteligentes.
- **Funcionalidades**: crear, listar, buscar por nombre, filtrar por marca y tipo, ordenar por fecha de lanzamiento, obtener detalle, actualizar y eliminar.
- **Entidad principal**: Device (nombre, descripción, brandId, deviceTypeId, releaseDate, imagen principal, lista de imágenes).
- **API REST**: `GET/POST /api/devices`, `GET/PUT/DELETE /api/devices/{id}`. Parámetros de filtro: `name`, `brandId`, `deviceTypeId`, `sortByReleaseDate`.
- **Base de datos**: `device_db` (tablas `devices`, `device_images`).

### 2. comment-service (puerto 8082)

- **Responsabilidad**: comentarios asociados a dispositivos.
- **Funcionalidades**: registrar comentario (deviceId, autor, texto) y listar comentarios por dispositivo.
- **Entidad principal**: Comment (deviceId, author, text, createdAt).
- **API REST**: `POST /api/comments`, `GET /api/comments?deviceId=...`.
- **Base de datos**: `comment_db` (tabla `comments`). `device_id` es referencia lógica al device-service.

### 3. catalog-service (puerto 8081)

- **Responsabilidad**: datos maestros (marcas y tipos de dispositivo).
- **Funcionalidades**: CRUD de marcas y CRUD de tipos de dispositivo.
- **Entidades**: Brand, DeviceType.
- **API REST**: `GET/POST/PUT/DELETE /api/brands`, `GET/POST/PUT/DELETE /api/device-types`.
- **Base de datos**: `catalog_db` (tablas `brands`, `device_types`).

---

## Diagrama general de la arquitectura

```
                    +------------------+
                    |  Angular SPA     |
                    |  (frontend)      |
                    +--------+--------+
                             |
         +-------------------+-------------------+
         |                   |                   |
         v                   v                   v
+----------------+  +----------------+  +----------------+
| device-service |  | catalog-service|  | comment-service|
| :8080          |  | :8081          |  | :8082          |
| (Hexagonal)    |  | (Hexagonal)    |  | (Hexagonal)    |
+-------+
        | REST
        v
+----------------+  +----------------+  +----------------+
|   device_db    |  |   catalog_db   |  |   comment_db   |
| (PostgreSQL)   |  | (PostgreSQL)   |  | (PostgreSQL)   |
+----------------+  +----------------+  +----------------+
```

Relaciones lógicas (sin FKs entre BDs):

- Un **dispositivo** pertenece a una **marca** (brandId → catalog-service) y tiene un **tipo** (deviceTypeId → catalog-service).
- Un **dispositivo** tiene muchos **comentarios** (deviceId en comment-service referencia al id del dispositivo en device-service).

---

## Estructura de un microservicio (hexagonal)

Ejemplo **device-service**:

```
device-service
├── application
│   ├── ports
│   │   ├── in          (DeviceUseCases)
│   │   └── out         (DeviceRepositoryPort)
│   └── usecase         (DeviceService)
├── domain
│   └── model           (Device - POJO)
├── infrastructure
│   ├── adapter
│   │   ├── in
│   │   │   └── rest    (DeviceRestController)
│   │   └── out
│   │       └── persistence  (DeviceJpaEntity, DeviceJpaRepository, DevicePersistenceAdapter)
│   └── config          (DeviceConfig - beans)
└── DeviceServiceApplication.java
```

- **Dominio**: solo modelos y reglas de negocio; sin Spring ni JPA.
- **Puertos**: interfaces que definen entrada (casos de uso) y salida (repositorios).
- **Adaptadores**: REST (entrada) y JPA (salida) implementan esos puertos.

---

## Instrucciones de ejecución

### Requisitos

- **Java 17**
- **Maven 3.8+**
- **PostgreSQL** (por ejemplo 14 o 15)
- **Node.js 18+** y **npm** (para Angular)

### 1. Base de datos

Crear las tres bases y cargar esquemas y datos de prueba:

```bash
# Crear bases (como usuario con permisos de creación)
psql -U postgres -f database/scripts-creacion-bases.sql

# Esquemas y datos de prueba
psql -U postgres -d catalog_db -f database/catalog-service.sql
psql -U postgres -d device_db -f database/device-service.sql
psql -U postgres -d comment_db -f database/comment-service.sql
```

Ajustar usuario y contraseña en los `application.yml` de cada servicio si no usas `postgres/postgres`.

### 2. Backend (microservicios)

Cada servicio se ejecuta por separado, desde su carpeta:

```bash
# Terminal 1 - Catalog (puerto 8081)
cd backend/catalog-service
mvn spring-boot:run

# Terminal 2 - Device (puerto 8080)
cd backend/device-service
mvn spring-boot:run

# Terminal 3 - Comment (puerto 8082)
cd backend/comment-service
mvn spring-boot:run
```

Verificar:

- http://localhost:8081/api/brands  
- http://localhost:8080/api/devices  
- http://localhost:8082/api/comments?deviceId=1  

### 3. Frontend (Angular)

```bash
cd frontend-angular
npm install
npm start
```

Abrir **http://localhost:4200**. La SPA consume:

- device-service en `http://localhost:8080`
- catalog-service en `http://localhost:8081`
- comment-service en `http://localhost:8082`

Si usas otros puertos o dominios, actualiza las URLs en los servicios Angular (`device.service.ts`, `catalog.service.ts`, `comment.service.ts`).

---

## Estructura de entregables

> **Nota**: Si tu entrega exige la convención `AA2_PrimerApellido_SegundoApellido_Nombres`, renombra la carpeta del proyecto a ese formato. La estructura interna es la misma.

```
AA2_DispositivosInteligentes/
├── frontend-angular/          # SPA Angular (listado, detalle, administración)
├── backend/
│   ├── device-service/        # Microservicio dispositivos (hexagonal)
│   ├── comment-service/       # Microservicio comentarios (hexagonal)
│   └── catalog-service/       # Microservicio catálogo (hexagonal)
├── database/
│   ├── catalog-service.sql    # Esquema + datos catálogo
│   ├── device-service.sql      # Esquema + datos dispositivos
│   ├── comment-service.sql    # Esquema + datos comentarios
│   ├── scripts-creacion-bases.sql
│   └── modelos-ER/            # Descripción ER por servicio
└── README.md                  # Este documento
```

---

## Consideraciones académicas

- Código **limpio y comentado**, estructura clara para fines educativos.
- **Sin sobreingeniería**: sin seguridad avanzada (JWT, OAuth), sin API Gateway ni servicio de descubrimiento, para mantener el foco en hexagonal y microservicios.
- **Relaciones entre servicios** solo por ID (referencias lógicas); el front-end o un futuro BFF pueden ensamblar datos de varios servicios si se requiere.

---

## Decisiones arquitectónicas resumidas

| Decisión | Motivo |
|----------|--------|
| Microservicios por dominio (device, comment, catalog) | Separación clara de responsabilidades y bases de datos independientes. |
| Arquitectura hexagonal en cada servicio | Dominio desacoplado de frameworks y adaptadores intercambiables. |
| Dominio sin JPA ni controllers | El núcleo no depende de infraestructura; facilita pruebas y cambios. |
| Referencias lógicas (brandId, deviceTypeId, deviceId) | Coherencia con “database per service”; sin FKs entre BDs. |
| REST y JSON | Simplicidad e interoperabilidad para un proyecto académico. |
| Angular + Bootstrap | SPA moderna con componentes separados y consumo de APIs mediante servicios. |
