package com.dispositivos.catalog.infrastructure.adapter.in.rest;

import com.dispositivos.catalog.application.ports.in.DeviceTypeUseCases;
import com.dispositivos.catalog.infrastructure.adapter.in.rest.dto.DeviceTypeRequest;
import com.dispositivos.catalog.infrastructure.adapter.in.rest.dto.DeviceTypeResponse;
import com.dispositivos.catalog.infrastructure.adapter.in.rest.mapper.DeviceTypeMapper;
import com.dispositivos.catalog.infrastructure.config.ApiDoc;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping(ApiRoutes.DEVICE_TYPES)
@RequiredArgsConstructor
@Tag(name = "Device Types", description = "CRUD de tipos de dispositivo")
public class DeviceTypeRestController {

    private final DeviceTypeUseCases deviceTypeUseCases;

    @PostMapping
    @Operation(summary = "Crear tipo de dispositivo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tipo creado"),
            @ApiResponse(responseCode = "400", description = ApiDoc.ERROR_400),
            @ApiResponse(responseCode = "404", description = ApiDoc.ERROR_404_GENERIC)
    })
    public ResponseEntity<DeviceTypeResponse> create(@Valid @RequestBody DeviceTypeRequest request) {
        var created =
                deviceTypeUseCases.create(request.getName(), request.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(DeviceTypeMapper.toResponse(created));
    }

    @GetMapping
    @Operation(summary = "Listar todos los tipos de dispositivo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de tipos")
    })
    public ResponseEntity<List<DeviceTypeResponse>> findAll() {
        return ResponseEntity.ok(DeviceTypeMapper.toResponseList(deviceTypeUseCases.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener tipo por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo encontrado"),
            @ApiResponse(responseCode = "404", description = ApiDoc.ERROR_404_DEVICE_TYPE)
    })
    public ResponseEntity<DeviceTypeResponse> findById(@PathVariable Long id) {
        return deviceTypeUseCases.findById(id)
                .map(DeviceTypeMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tipo de dispositivo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo actualizado"),
            @ApiResponse(responseCode = "400", description = ApiDoc.ERROR_400),
            @ApiResponse(responseCode = "404", description = ApiDoc.ERROR_404_DEVICE_TYPE)
    })
    public ResponseEntity<DeviceTypeResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody DeviceTypeRequest request) {
        var updated =
                deviceTypeUseCases.update(id, request.getName(), request.getDescription());
        return ResponseEntity.ok(DeviceTypeMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tipo de dispositivo")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tipo eliminado")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deviceTypeUseCases.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
