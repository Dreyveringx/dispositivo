# Propuestas de mejora – Reducción de verbosidad (Backend)

Análisis de **catalog-service**, **device-service** y **comment-service**. Objetivo: reducir código verboso sin cambiar comportamiento ni romper arquitectura ni contratos REST.

---

## 1. Controllers REST

### 1.1 DeviceRestController – Muchos parámetros al use case (create/update)

**Archivos afectados:**  
- `device-service/.../DeviceRestController.java`  
- `device-service/.../DeviceUseCases.java`  
- `device-service/.../DeviceService.java`

**Problema:** El controller y el puerto reciben 7 parámetros primitivos en create y 8 en update. Cualquier campo nuevo obliga a tocar firma del puerto, use case y controller.

**Propuesta:** Introducir un **command de aplicación** (solo en device-service) para create/update: el controller mapea `DeviceRequest` → command y el use case recibe un solo objeto. El contrato REST (JSON, URLs, códigos) no cambia.

**ANTES (fragmento):**

```java
// DeviceUseCases.java
Device create(String name, String description, Long brandId, Long deviceTypeId,
              LocalDate releaseDate, String imageUrl, List<String> imageUrls);
Device update(Long id, String name, String description, Long brandId, Long deviceTypeId,
              LocalDate releaseDate, String imageUrl, List<String> imageUrls);

// DeviceRestController.java
var created = deviceUseCases.create(
    request.getName(), request.getDescription(), request.getBrandId(),
    request.getDeviceTypeId(), request.getReleaseDate(), request.getImageUrl(),
    request.getImageUrls());
// update similar con 8 argumentos
```

**DESPUÉS (ejemplo):**

```java
// application/ports/in/DeviceCommand.java (nuevo, en application)
public record DeviceCommand(String name, String description, Long brandId, Long deviceTypeId,
                            LocalDate releaseDate, String imageUrl, List<String> imageUrls) {}

// DeviceUseCases.java
Device create(DeviceCommand command);
Device update(Long id, DeviceCommand command);

// DeviceRestController.java
var cmd = new DeviceCommand(request.getName(), request.getDescription(), request.getBrandId(),
    request.getDeviceTypeId(), request.getReleaseDate(), request.getImageUrl(),
    request.getImageUrls());
var created = deviceUseCases.create(cmd);
```

**Impacto:** Medio (cambia solo puerto y use case de device; el JSON y el controller siguen igual desde fuera).

---

### 1.2 Repetición de @ApiResponses (400/404) en todos los controllers

**Archivos afectados:**  
- `BrandRestController.java`, `DeviceTypeRestController.java`, `DeviceRestController.java`, `CommentRestController.java`

**Problema:** Los mismos códigos 400 y 404 se repiten en casi todos los endpoints con descripciones largas idénticas.

**Propuesta:** Definir **constantes** para las descripciones de error y reutilizarlas en `@ApiResponse(..., description = ApiDoc.ERROR_400)` (o una anotación compuesta si se prefiere). No cambiar estructura OpenAPI.

**ANTES:**

```java
@ApiResponses({
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (RFC 7807 Problem Detail)"),
    @ApiResponse(responseCode = "404", description = "Marca no encontrada (RFC 7807 Problem Detail)")
})
```

**DESPUÉS (ejemplo):**

```java
// En cada servicio: rest/ApiDoc.java o config/ApiDoc.java
public final class ApiDoc {
    private ApiDoc() {}
    public static final String ERROR_400 = "Datos de entrada inválidos (RFC 7807 Problem Detail)";
    public static final String ERROR_404_NOT_FOUND = "Recurso no encontrado (RFC 7807 Problem Detail)";
    public static final String ERROR_404_BRAND = "Marca no encontrada (RFC 7807 Problem Detail)";
    // ...
}

@ApiResponses({
    @ApiResponse(responseCode = "400", description = ApiDoc.ERROR_400),
    @ApiResponse(responseCode = "404", description = ApiDoc.ERROR_404_BRAND)
})
```

**Impacto:** Bajo (solo strings centralizados; Swagger sigue igual).

---

### 1.3 ResponseEntity – Posible acortado solo en creación

**Archivos afectados:**  
- Todos los controllers con `ResponseEntity.status(HttpStatus.CREATED).body(...)`.

**Problema:** Verboso; se repite el mismo patrón en todos los POST.

**Propuesta (opcional):** Método estático de utilidad en el mismo paquete o en un pequeño helper, por ejemplo `Responses.created(body)`, que devuelva `ResponseEntity.status(HttpStatus.CREATED).body(body)`. Opcional porque la ganancia es pequeña.

**Impacto:** Bajo.

---

## 2. Use cases / Application layer

### 2.1 DeviceService – Métodos create/update con muchos parámetros

**Archivos afectados:**  
- `device-service/.../DeviceUseCases.java`  
- `device-service/.../DeviceService.java`

**Problema:** Misma que 1.1: 7 u 8 parámetros por método; difícil de leer y de extender.

**Propuesta:** Ver **1.1**: command de aplicación (`DeviceCommand`) y firmas `create(DeviceCommand)`, `update(Long id, DeviceCommand)`. La lógica de negocio (validaciones, orElseThrow, save) se mantiene igual; solo cambia la forma de recibir los datos.

**Impacto:** Medio (va junto con 1.1).

---

### 2.2 BrandService / DeviceTypeService – Pocos parámetros

**Archivos afectados:**  
- `catalog-service/.../BrandService.java`, `DeviceTypeService.java`  
- Puertos `BrandUseCases`, `DeviceTypeUseCases`.

**Problema:** Solo 2 parámetros (name, description). La verbosidad es baja.

**Propuesta:** No refactorizar por ahora. Si se quiere homogeneidad con device, más adelante se podría introducir `BrandCommand(name, description)` y análogo para DeviceType. No prioritario.

**Impacto:** N/A (no aplicar o bajo si se hace por consistencia).

---

## 3. Mappers

### 3.1 DeviceMapper – Lista null-safe de imageUrls

**Archivo afectado:**  
- `device-service/.../mapper/DeviceMapper.java`

**Problema:** La lógica `domain.getImageUrls() != null ? List.copyOf(...) : Collections.emptyList()` se repite conceptualmente en el mapper y en el persistence adapter (lista segura).

**Propuesta:** Extraer un **método privado estático** en el mismo mapper, por ejemplo `copyImageUrls(List<String> urls)`, y usarlo en `toResponse`. No tocar contrato ni JSON.

**ANTES:**

```java
dto.setImageUrls(domain.getImageUrls() != null
        ? List.copyOf(domain.getImageUrls())
        : Collections.emptyList());
```

**DESPUÉS:**

```java
dto.setImageUrls(copyImageUrls(domain.getImageUrls()));

private static List<String> copyImageUrls(List<String> urls) {
    return urls == null || urls.isEmpty() ? Collections.emptyList() : List.copyOf(urls);
}
```

**Impacto:** Bajo (solo legibilidad y un solo lugar para la regla “lista segura”).

---

### 3.2 DevicePersistenceAdapter – Lista segura en toDomain

**Archivo afectado:**  
- `device-service/.../DevicePersistenceAdapter.java`

**Problema:** En `toDomain` se hace `urls != null ? new ArrayList<>(urls) : new ArrayList<>()`. Misma idea que en DeviceMapper.

**Propuesta:** Reutilizar un helper estático (por ejemplo en el mismo adapter o en un util de paquete `persistence`) para “lista null-safe” y usarlo en `toEntity` y `toDomain` donde corresponda. Sin cambiar comportamiento.

**Impacto:** Bajo.

---

### 3.3 Duplicación toEntity / toDomain (Device)

**Archivo afectado:**  
- `device-service/.../DevicePersistenceAdapter.java`

**Problema:** toEntity y toDomain son inversos y tienen muchos campos; no hay duplicación de lógica más allá de la lista de campos.

**Propuesta:** No unificar en un solo método: mantener dos métodos explícitos (toEntity / toDomain) para claridad y evolución independiente de Domain vs JPA. Solo reducir verbosidad de listas con el helper de 3.2.

**Impacto:** N/A (no refactorizar aquí).

---

## 4. DTOs

### 4.1 DeviceRequest – Estilo Lombok

**Archivo afectado:**  
- `device-service/.../dto/DeviceRequest.java`

**Problema:** Uso de `@lombok.Data` (nombre fully qualified); en otros DTOs se usa `@Data`. Poca consistencia de estilo.

**Propuesta:** Usar `import lombok.Data;` y anotación `@Data` como en el resto del proyecto.

**ANTES:**

```java
@lombok.Data
@Schema(...)
public class DeviceRequest {
```

**DESPUÉS:**

```java
import lombok.Data;
// ...
@Data
@Schema(...)
public class DeviceRequest {
```

**Impacto:** Bajo (solo estilo).

---

### 4.2 Request/Response con mismos campos (Brand, DeviceType)

**Archivos afectados:**  
- `BrandRequest` / `BrandResponse`, `DeviceTypeRequest` / `DeviceTypeResponse`

**Problema:** Request y Response comparten nombre y tipo de campos (name, description). No hay herencia ni reutilización.

**Propuesta:** Mantener DTOs separados (Request vs Response) para no acoplar contrato de entrada al de salida y permitir evolución distinta. No unificar en una sola clase. Opcional: si en el futuro se añaden más DTOs muy similares, se podría valorar una interfaz común o un record base; no aplicable aún.

**Impacto:** N/A (no cambiar).

---

### 4.3 Records para DTOs (Java 17+)

**Archivos afectados:**  
- Cualquier DTO sin lógica (por ejemplo `BrandRequest`, `BrandResponse`).

**Problema:** Clases con solo campos y getters/setters son verbosas.

**Propuesta (opcional):** Migrar DTOs simples a `record` cuando no tengan validaciones complechas o cuando las validaciones sigan funcionando en el constructor compacto. Ejemplo: `public record BrandResponse(Long id, String name, String description) {}`. Requiere comprobar que Swagger y Jackson sigan generando el mismo JSON.

**Impacto:** Medio (cambios en varios DTOs y pruebas de serialización/validación).

---

## 5. Configuración y estilo

### 5.1 Lombok – Uso consistente

**Archivos afectados:**  
- `Device.java`, `DeviceJpaEntity.java`, `DeviceRequest.java` (y cualquier otro que use `@lombok.X`).

**Problema:** Mezcla de `@lombok.Data` y `@Data` con import.

**Propuesta:** Estandarizar en `import lombok.Data;` (y lo mismo para `@Getter`, `@Setter`, etc.) en todo el backend.

**Impacto:** Bajo.

---

### 5.2 Use cases – Constructores explícitos

**Archivos afectados:**  
- `BrandService`, `DeviceTypeService`, `DeviceService`, `CommentService`.

**Problema:** Constructores escritos a mano con un solo campo inyectado.

**Propuesta (opcional):** Añadir `@RequiredArgsConstructor` y campo `private final` para el repositorio, y eliminar el constructor explícito. Mantiene el mismo comportamiento y reduce líneas.

**ANTES:**

```java
public class DeviceService implements DeviceUseCases {
    private final DeviceRepositoryPort deviceRepository;
    public DeviceService(DeviceRepositoryPort deviceRepository) {
        this.deviceRepository = deviceRepository;
    }
```

**DESPUÉS:**

```java
@RequiredArgsConstructor
public class DeviceService implements DeviceUseCases {
    private final DeviceRepositoryPort deviceRepository;
```

**Impacto:** Bajo (solo aplicación layer; no toca puertos ni REST).

---

## 6. Resumen y prioridad

| # | Mejora | Archivo(s) | Impacto | Prioridad |
|---|--------|------------|---------|-----------|
| 1.1 + 2.1 | Command para create/update en device | DeviceUseCases, DeviceService, DeviceRestController, nuevo DeviceCommand | Medio | Alta (mantenibilidad) |
| 1.2 | Constantes para descripciones @ApiResponse | Todos los controllers + nuevo ApiDoc (por servicio) | Bajo | Media |
| 3.1 | Helper copyImageUrls en DeviceMapper | DeviceMapper | Bajo | Baja |
| 3.2 | Helper lista null-safe en DevicePersistenceAdapter | DevicePersistenceAdapter | Bajo | Baja |
| 4.1 + 5.1 | Lombok consistente (@Data con import) | DeviceRequest, Device, DeviceJpaEntity, etc. | Bajo | Baja |
| 5.2 | @RequiredArgsConstructor en use cases | BrandService, DeviceTypeService, DeviceService, CommentService | Bajo | Baja |
| 1.3 | Helper Responses.created (opcional) | Controllers | Bajo | Opcional |
| 4.3 | Records para DTOs (opcional) | Varios DTOs | Medio | Opcional |

---

## 7. Restricciones respetadas

- No se cambia lógica de negocio.  
- No se modifican endpoints, JSON ni códigos HTTP.  
- No se introduce ningún framework nuevo.  
- No se mueve lógica desde use cases al controller.  
- Los cambios propuestos son acotados y con impacto valorado (bajo/medio).

Si quieres, el siguiente paso puede ser implementar solo las de **impacto bajo** (1.2, 3.1, 4.1, 5.1, 5.2) o, además, la de **DeviceCommand** (1.1 + 2.1) en device-service.
