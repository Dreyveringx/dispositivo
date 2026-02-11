package com.dispositivos.device.infrastructure.adapter.in.rest;

import com.dispositivos.device.application.ports.in.DeviceCommand;
import com.dispositivos.device.application.ports.in.DeviceUseCases;
import com.dispositivos.device.infrastructure.adapter.in.rest.dto.DeviceRequest;
import com.dispositivos.device.infrastructure.adapter.in.rest.dto.DeviceResponse;
import com.dispositivos.device.infrastructure.adapter.in.rest.mapper.DeviceMapper;
import com.dispositivos.device.infrastructure.config.ApiDoc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiRoutes.DEVICES)
@RequiredArgsConstructor
@Tag(name = "Devices", description = "CRUD de dispositivos inteligentes")
public class DeviceRestController {

    private final DeviceUseCases deviceUseCases;

    @PostMapping
    @Operation(summary = "Crear dispositivo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Dispositivo creado"),
            @ApiResponse(responseCode = "400", description = ApiDoc.ERROR_400),
            @ApiResponse(responseCode = "404", description = ApiDoc.ERROR_404_GENERIC)
    })
    public ResponseEntity<DeviceResponse> create(@Valid @RequestBody DeviceRequest request) {
        DeviceCommand command = toCommand(request);
        var created = deviceUseCases.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(DeviceMapper.toResponse(created));
    }

    @GetMapping
    @Operation(summary = "Listar dispositivos con filtros opcionales")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de dispositivos")
    })
    public ResponseEntity<List<DeviceResponse>> list(
            @Parameter(description = "Filtrar por nombre (contiene)") @RequestParam(required = false) String name,
            @Parameter(description = "Filtrar por ID de marca") @RequestParam(required = false) Long brandId,
            @Parameter(description = "Filtrar por ID de tipo") @RequestParam(required = false) Long deviceTypeId,
            @Parameter(description = "Ordenar por fecha de lanzamiento descendente") @RequestParam(defaultValue = "true") boolean sortByReleaseDate
    ) {
        return ResponseEntity.ok(
                DeviceMapper.toResponseList(
                        deviceUseCases.findFiltered(name, brandId, deviceTypeId, sortByReleaseDate)
                )
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener dispositivo por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dispositivo encontrado"),
            @ApiResponse(responseCode = "404", description = ApiDoc.ERROR_404_DEVICE)
    })
    public ResponseEntity<DeviceResponse> getById(@PathVariable Long id) {
        return deviceUseCases.findById(id)
                .map(DeviceMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar dispositivo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dispositivo actualizado"),
            @ApiResponse(responseCode = "400", description = ApiDoc.ERROR_400),
            @ApiResponse(responseCode = "404", description = ApiDoc.ERROR_404_DEVICE)
    })
    public ResponseEntity<DeviceResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody DeviceRequest request) {
        DeviceCommand command = toCommand(request);
        var updated = deviceUseCases.update(id, command);
        return ResponseEntity.ok(DeviceMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar dispositivo")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Dispositivo eliminado")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deviceUseCases.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private static DeviceCommand toCommand(DeviceRequest request) {
        return new DeviceCommand(
                request.getName(),
                request.getDescription(),
                request.getBrandId(),
                request.getDeviceTypeId(),
                request.getReleaseDate(),
                request.getImageUrl(),
                request.getImageUrls()
        );
    }
}
