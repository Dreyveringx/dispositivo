# INFORME FINAL DE PROYECTO
## Actividad académica: Creando con patrón de arquitectura

**Proyecto:** AA2_DispositivosInteligentes  
**Asignatura:** Creación de ambientes de desarrollo web en servidores remotos  
**Contenidos temáticos:** Introducción a la programación Back-end, Lenguajes para el Back-end, Arquitecturas Back-end  

---

# PARTE 1 — INFORME FINAL

## 1. Introducción

El presente informe documenta el desarrollo del sitio web **Dispositivos Inteligentes**, elaborado en el marco de la actividad académica “Creando con patrón de arquitectura”. El sistema permite consultar un catálogo de dispositivos inteligentes (asistentes de voz, termostatos, bombillas, sensores, etc.), aplicar filtros y búsqueda, visualizar el detalle de cada dispositivo con galería de imágenes (zoom y pan), gestionar comentarios por dispositivo y administrar (CRUD) dispositivos, marcas y tipos desde un panel de administración.

La solución se ha implementado con **arquitectura de microservicios** en el back-end (Spring Boot), **arquitectura hexagonal** en cada microservicio, front-end en **Angular** con **Bootstrap**, bases de datos **PostgreSQL** (una por microservicio) y comunicación **REST** entre el front-end y los servicios.

## 2. Objetivo del proyecto

- Desarrollar un sitio web funcional sobre dispositivos inteligentes que cumpla los requisitos funcionales y técnicos de la actividad.
- Aplicar un patrón arquitectónico (en este caso, microservicios con arquitectura hexagonal) de forma coherente en el back-end.
- Demostrar el uso de buenas prácticas en front-end (Angular/Bootstrap), diseño de base de datos y estructura de carpetas adecuada para la entrega.

## 3. Descripción general de la solución

El sistema se compone de:

- **Front-end (SPA Angular):** Página inicial con listado de dispositivos, filtros por marca y tipo, búsqueda por nombre y orden por fecha de lanzamiento; vista detalle con información del dispositivo, galería de imágenes con zoom y pan, y sistema de comentarios (lectura y publicación); panel de administración con pestañas para CRUD de dispositivos, marcas y tipos.
- **Back-end:** Tres microservicios Spring Boot (device-service, catalog-service, comment-service), cada uno con su propia base PostgreSQL y estructura hexagonal (dominio, puertos, adaptadores).
- **Comunicación:** El front-end consume las APIs REST de cada microservicio (puertos 8080, 8081 y 8082). No hay comunicación entre microservicios; las relaciones se resuelven por referencias lógicas (identificadores) y el front-end ensambla la información cuando es necesario.

## 4. Arquitectura implementada

### 4.1 Microservicios

Se han implementado tres microservicios:

| Microservicio    | Puerto | Responsabilidad                                      | Base de datos |
|------------------|--------|------------------------------------------------------|---------------|
| device-service   | 8080   | Dispositivos: CRUD, listado, filtros, búsqueda, orden por fecha | device_db     |
| catalog-service  | 8081   | Datos maestros: marcas y tipos de dispositivo       | catalog_db    |
| comment-service  | 8082   | Comentarios por dispositivo                         | comment_db    |

Cada servicio se despliega de forma independiente y utiliza su propia base de datos PostgreSQL, alineado con el patrón “database per service”.

### 4.2 Justificación de microservicios

- **Separación de responsabilidades:** Cada servicio tiene un dominio acotado (dispositivos, catálogo, comentarios), lo que facilita el mantenimiento y la comprensión.
- **Escalabilidad independiente:** Se puede escalar solo el servicio que soporta mayor carga (por ejemplo, comentarios) sin modificar los demás.
- **Base de datos por servicio:** device_db, catalog_db y comment_db son independientes; no hay claves foráneas entre bases. Las relaciones se manejan por identificadores (brandId, deviceTypeId, deviceId) a nivel de aplicación.
- **Adecuación académica:** Permite trabajar explícitamente límites de servicios, APIs REST y referencias lógicas entre dominios.

### 4.3 Arquitectura hexagonal

En cada microservicio se aplica la arquitectura hexagonal (Ports & Adapters):

- **Dominio (centro):** Modelos de dominio (Device, Comment, Brand, DeviceType) como POJOs sin anotaciones JPA ni dependencias de Spring. La lógica de negocio no depende de HTTP ni de la base de datos.
- **Puertos de entrada (in):** Interfaces que definen los casos de uso (por ejemplo, `DeviceUseCases`, `CommentUseCases`, `BrandUseCases`). El adaptador REST invoca estos puertos.
- **Puertos de salida (out):** Interfaces de persistencia (por ejemplo, `DeviceRepositoryPort`, `CommentRepositoryPort`). Los adaptadores de infraestructura (JPA) implementan estos puertos y traducen entre dominio y entidades JPA.
- **Adaptadores:** REST (entrada) y JPA (salida) se ubican en la capa de infraestructura; el dominio permanece desacoplado de frameworks y permite sustituir adaptadores (por ejemplo, otro almacén o otra API) sin tocar el núcleo.

La configuración (por ejemplo, `DeviceConfig`) define los beans que inyectan los adaptadores en los casos de uso, cumpliendo inversión de dependencias.

## 5. Tecnologías utilizadas

| Capa / Componente | Tecnología |
|--------------------|------------|
| Front-end          | Angular 17, Bootstrap 5.3, RxJS, TypeScript 5.4 |
| Back-end           | Java 17, Spring Boot, Spring Data JPA, Maven |
| Base de datos      | PostgreSQL (tres bases: device_db, catalog_db, comment_db) |
| Comunicación       | REST (JSON), HttpClient (Angular) |
| Estructura back-end| Arquitectura hexagonal por microservicio |

## 6. Descripción del Frontend

- **Framework y UI:** Angular 17 (standalone components) y Bootstrap 5.3 (incluido vía CDN en `index.html`). Uso de clases de Bootstrap para layout (grid, cards, forms, tabs, botones) y estilos globales en `styles.css` para personalización (colores, sombras, hover).
- **Estructura:** `app/pages` (home, device-detail, admin), `app/services` (device, catalog, comment), `app/models` (device, brand, device-type, comment). Rutas definidas en `app.routes.ts` con lazy loading de componentes.
- **Página inicial (Home):** Listado de dispositivos en tarjetas; filtros por marca y tipo (selects); búsqueda por nombre (input con enlace a `loadDevices()`); orden por fecha de lanzamiento (más recientes / más antiguos). Consumo de device-service y catalog-service para datos y filtros.
- **Vista detalle (Device Detail):** Información del dispositivo (nombre, marca, tipo, fecha de lanzamiento, descripción); galería de imágenes con visor principal que implementa zoom (rueda del ratón y botones Zoom + / − / Reset) y pan (arrastre con ratón cuando zoom > 1); miniaturas para cambiar la imagen mostrada. Sección de comentarios: listado por dispositivo y formulario para publicar (autor y texto). Consumo de device-service, catalog-service y comment-service.
- **Panel de administración (Admin):** Pestañas para Dispositivos, Marcas y Tipos. En cada pestaña: tabla con listado, botones Editar/Eliminar y formulario para crear/editar (CRUD completo). Dispositivos incluyen nombre, descripción, marca, tipo, fecha de lanzamiento y múltiples URLs de imágenes (FormArray). Servicios Angular realizan GET/POST/PUT/DELETE contra las APIs correspondientes.

## 7. Descripción del Backend

- **device-service:** API REST en `/api/devices` (GET con parámetros name, brandId, deviceTypeId, sortByReleaseDate; GET por id; POST, PUT, DELETE). Dominio: modelo `Device` (POJO). Puertos: `DeviceUseCases` (entrada), `DeviceRepositoryPort` (salida). Adaptadores: `DeviceRestController`, `DevicePersistenceAdapter` con JPA (`DeviceJpaEntity`, `DeviceJpaRepository`) y tabla `device_images` para URLs adicionales.
- **catalog-service:** APIs `/api/brands` y `/api/device-types` con CRUD completo. Dominio: `Brand`, `DeviceType`. Puertos y adaptadores análogos (BrandService/DeviceTypeService, BrandRestController/DeviceTypeRestController, persistencia JPA en catalog_db).
- **comment-service:** `POST /api/comments` y `GET /api/comments?deviceId=...`. Dominio: `Comment`. Puertos y adaptadores: CommentUseCases, CommentRestController, CommentPersistenceAdapter sobre comment_db.

Cada servicio tiene `application.yml` con puerto, nombre de aplicación y configuración de datasource (PostgreSQL). El dominio no contiene anotaciones JPA ni referencias a Spring MVC; los controladores y entidades JPA están únicamente en la capa de infraestructura.

## 8. Diseño de base de datos

- **device_db:** Tabla `devices` (id, name, description, brand_id, device_type_id, release_date, image_url) y tabla `device_images` (device_id FK a devices, url). brand_id y device_type_id son referencias lógicas al catalog-service. Índices en brand_id, device_type_id, release_date y name.
- **catalog_db:** Tablas `brands` (id, name, description) y `device_types` (id, name, description), sin relación entre sí. Índices por nombre.
- **comment_db:** Tabla `comments` (id, device_id, author, text, created_at). device_id es referencia lógica al device-service. Índices en device_id y created_at.

En la carpeta `database/modelos-ER` se documentan los modelos entidad-relación por servicio (entidades, atributos, relaciones y diagramas en texto), coherentes con los scripts SQL de cada base.

## 9. Cumplimiento de requisitos

El proyecto cumple con los requisitos funcionales y técnicos de la actividad:

1. **Sitio web sobre dispositivos inteligentes:** Implementado como SPA Angular que consume tres microservicios.
2. **Página inicial:** Listado de dispositivos, orden por fecha de lanzamiento (más recientes / más antiguos), filtros por marca y tipo, búsqueda por nombre.
3. **Vista detalle:** Información del dispositivo e imágenes en galería con zoom y pan.
4. **Sistema de comentarios por dispositivo:** Listado y alta de comentarios en la vista detalle, respaldado por comment-service.
5. **Sistema de administración (CRUD):** Panel con pestañas para CRUD de dispositivos, marcas y tipos.
6. **Diseño de base de datos:** Tres bases PostgreSQL con esquemas definidos en scripts SQL y documentación ER en `database/modelos-ER`.
7. **Buenas prácticas (Angular/Bootstrap):** Componentes standalone, servicios inyectables, uso de Bootstrap para layout y componentes, formularios reactivos en admin, lazy loading de rutas.
8. **Arquitectura:** Microservicios con arquitectura hexagonal (puertos y adaptadores) en cada servicio.
9. **Modelo entidad-relación:** Elaborado y documentado por servicio en `database/modelos-ER`.
10. **Estructura de carpetas:** Organización clara: `frontend-angular/`, `backend/` (device-service, comment-service, catalog-service), `database/` (scripts SQL y modelos-ER), y `README.md` con instrucciones de ejecución y descripción de la arquitectura.

## 10. Conclusiones

El proyecto **AA2_DispositivosInteligentes** cumple con los objetivos de la actividad “Creando con patrón de arquitectura”. Se ha implementado un sitio web funcional sobre dispositivos inteligentes con página inicial (listado, filtros, búsqueda y orden por fecha), vista detalle con galería (zoom y pan) y comentarios, y panel de administración con CRUD completo. La arquitectura de microservicios y la aplicación de arquitectura hexagonal en cada servicio permiten separar responsabilidades, mantener el dominio independiente del framework y de la persistencia, y alinear la solución con contenidos de back-end y arquitecturas. El diseño de base de datos (una BD por microservicio, referencias lógicas) y la documentación ER reflejan de forma coherente el modelo de datos. La estructura de carpetas y el README facilitan la revisión y la ejecución del proyecto en un entorno académico.

---

# PARTE 2 — CHECKLIST DE CUMPLIMIENTO

| # | Requisito | ¿Cumple? | Evidencia | Observaciones |
|---|-----------|-----------|-----------|----------------|
| 1 | Sitio web sobre dispositivos inteligentes | **Sí** | SPA Angular (frontend-angular) que consume device-service, catalog-service y comment-service; listado y detalle de dispositivos. | Proyecto completo en AA2_DispositivosInteligentes. |
| 2 | Página inicial con listado de dispositivos | **Sí** | HomeComponent: tarjetas con nombre, fecha de lanzamiento y enlace “Ver detalle”. | `frontend-angular/src/app/pages/home/home.component.ts`. |
| 3 | Orden por fecha de lanzamiento | **Sí** | Select “Ordenar” con opciones “Más recientes primero” / “Más antiguos primero”; parámetro `sortByReleaseDate` en getList(). | HomeComponent template y DeviceService.getList(); device-service GET /api/devices?sortByReleaseDate=. |
| 4 | Filtros (marca, tipo) | **Sí** | Selects Marca y Tipo en la página inicial; filtros enviados como brandId y deviceTypeId a la API. | HomeComponent; DeviceRestController con @RequestParam brandId, deviceTypeId. |
| 5 | Búsqueda | **Sí** | Input “Buscar por nombre” en la página inicial; parámetro name enviado a la API. | HomeComponent; DeviceRestController con @RequestParam name. |
| 6 | Vista detalle con información del dispositivo e imágenes | **Sí** | Ruta /device/:id; DeviceDetailComponent muestra nombre, marca, tipo, fecha, descripción y galería de imágenes. | device-detail.component.ts; DeviceService.getById(). |
| 7 | Galería con zoom y pan | **Sí** | Visor con zoom (rueda + botones Zoom + / − / Reset), pan con arrastre (mousedown/mousemove/mouseup), miniaturas para cambiar imagen. | device-detail.component.ts: getViewerTransform(), onViewerWheel(), onViewerMouseDown(), onDocumentMouseMove(), zoomIn/Out/resetZoom. |
| 8 | Sistema de comentarios por dispositivo | **Sí** | Sección “Comentarios” en vista detalle: listado y formulario (autor, texto); CommentService create y getByDeviceId. | device-detail.component.ts; comment.service.ts; comment-service REST. |
| 9 | Sistema de administración (CRUD) | **Sí** | Panel /admin con pestañas Dispositivos, Marcas, Tipos; en cada una: tabla, Crear, Editar, Eliminar. | admin.component.ts; DeviceService/CatalogService create, update, delete. |
| 10 | Diseño de base de datos | **Sí** | Tres bases PostgreSQL: device_db (devices, device_images), catalog_db (brands, device_types), comment_db (comments). Scripts en database/*.sql. | database/scripts-creacion-bases.sql, device-service.sql, catalog-service.sql, comment-service.sql. |
| 11 | Buenas prácticas (Angular / Bootstrap) | **Sí** | Angular: servicios inyectables, modelos tipados, rutas con lazy load, formularios reactivos en admin. Bootstrap: grid, cards, forms, tabs, botones (index.html carga Bootstrap 5.3). | package.json (Angular 17, Bootstrap 5.3); index.html; componentes con clases Bootstrap. |
| 12 | Implementación de arquitectura (capas, eventos o microservicios) | **Sí** | Tres microservicios: device-service, catalog-service, comment-service; comunicación REST; una BD por servicio. | backend/; README.md; application.yml por servicio. |
| 13 | Arquitectura hexagonal (como detalle del 12) | **Sí** | En cada servicio: domain (POJOs), application/ports (in/out), application/usecase, infrastructure adapters (REST, JPA), config (beans). | Ej. device-service: Device, DeviceUseCases, DeviceRepositoryPort, DeviceService, DeviceRestController, DevicePersistenceAdapter, DeviceConfig. |
| 14 | Modelo entidad-relación elaborado | **Sí** | Documentos por servicio con entidades, atributos, relaciones y diagramas. | database/modelos-ER/device-service-er.md, catalog-service-er.md, comment-service-er.md. |
| 15 | Estructura de carpetas correcta para la entrega | **Sí** | Raíz: frontend-angular/, backend/ (device-service, comment-service, catalog-service), database/ (scripts SQL y modelos-ER), README.md. | Estructura descrita en README y presente en el repositorio. |

---

# PARTE 3 — OBSERVACIONES DEL DOCENTE

## Fortalezas del proyecto

- **Arquitectura bien aplicada:** La separación en microservicios (device, catalog, comment) y la aplicación consistente de arquitectura hexagonal en cada uno (dominio sin JPA ni controllers, puertos de entrada/salida, adaptadores REST y JPA) muestran comprensión de los patrones y de la inversión de dependencias.
- **Documentación:** El README explica con claridad la justificación de microservicios y de la arquitectura hexagonal, el diagrama de arquitectura, la estructura de cada microservicio y las instrucciones de ejecución (BD, backend, frontend), lo que facilita la revisión y la réplica del proyecto.
- **Funcionalidad completa:** Cubre todos los requisitos: listado, filtros, búsqueda, orden por fecha, vista detalle, galería con zoom y pan, comentarios y CRUD de dispositivos, marcas y tipos.
- **Base de datos:** Diseño coherente con “database per service”, referencias lógicas entre dominios y documentación ER por servicio.
- **Front-end ordenado:** Uso de servicios por dominio (device, catalog, comment), modelos tipados, rutas con lazy loading y panel de administración con formularios reactivos y pestañas claras.

## Buenas prácticas aplicadas

- Dominio desacoplado de infraestructura (POJOs sin anotaciones de framework).
- Interfaces (puertos) para casos de uso y persistencia, permitiendo pruebas y sustitución de adaptadores.
- Una base de datos por microservicio y relaciones solo por identificadores.
- Front-end con componentes standalone, servicios `providedIn: 'root'` y consumo explícito de APIs REST.
- Uso de Bootstrap para UI consistente y estilos globales personalizados sin romper la estructura.
- Estructura de carpetas y nomenclatura coherentes en backend y frontend.

## Nivel del proyecto

**Avanzado.** El proyecto no se limita a una aplicación monolítica con capas clásicas: incorpora microservicios, arquitectura hexagonal en cada servicio, tres bases de datos independientes y un front-end SPA que orquesta varias APIs. La galería con zoom y pan y el CRUD completo con formularios reactivos refuerzan un nivel por encima de lo básico o intermedio.

## Recomendaciones (opcionales)

- Incluir en el informe o en el README una sección “Cómo probar los requisitos” con pasos concretos (ej.: “Filtros: en la página inicial elegir una marca y comprobar que el listado se actualiza”), para facilitar la verificación por el docente.
- A medio plazo, valorar la incorporación de un API Gateway o de un BFF si se añaden más servicios o políticas de seguridad (CORS, autenticación), manteniendo la misma arquitectura de microservicios.
- Documentar en el README la versión de PostgreSQL utilizada (ej. 14 o 15) si se detectan diferencias de comportamiento entre versiones.

---

*Documento generado para entrega académica. No se ha modificado código del proyecto; el contenido se basa en la estructura y funcionalidad existente en el repositorio AA2_DispositivosInteligentes.*
