package com.dispositivos.catalog.infrastructure.adapter.in.rest;

import com.dispositivos.catalog.application.ports.in.BrandUseCases;
import com.dispositivos.catalog.infrastructure.adapter.in.rest.dto.BrandRequest;
import com.dispositivos.catalog.infrastructure.adapter.in.rest.dto.BrandResponse;
import com.dispositivos.catalog.infrastructure.adapter.in.rest.mapper.BrandMapper;
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
@RequestMapping(ApiRoutes.BRANDS)
@RequiredArgsConstructor
@Tag(name = "Brands", description = "CRUD de marcas")
public class BrandRestController {

    private final BrandUseCases brandUseCases;

    @PostMapping
    @Operation(summary = "Crear marca")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Marca creada"),
            @ApiResponse(responseCode = "400", description = ApiDoc.ERROR_400),
            @ApiResponse(responseCode = "404", description = ApiDoc.ERROR_404_GENERIC)
    })
    public ResponseEntity<BrandResponse> create(@Valid @RequestBody BrandRequest request) {
        var created = brandUseCases.create(request.getName(), request.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(BrandMapper.toResponse(created));
    }

    @GetMapping
    @Operation(summary = "Listar todas las marcas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de marcas")
    })
    public ResponseEntity<List<BrandResponse>> findAll() {
        return ResponseEntity.ok(BrandMapper.toResponseList(brandUseCases.findAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener marca por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Marca encontrada"),
            @ApiResponse(responseCode = "404", description = ApiDoc.ERROR_404_BRAND)
    })
    public ResponseEntity<BrandResponse> findById(@PathVariable Long id) {
        return brandUseCases.findById(id)
                .map(BrandMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar marca")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Marca actualizada"),
            @ApiResponse(responseCode = "400", description = ApiDoc.ERROR_400),
            @ApiResponse(responseCode = "404", description = ApiDoc.ERROR_404_BRAND)
    })
    public ResponseEntity<BrandResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody BrandRequest request) {
        var updated = brandUseCases.update(id, request.getName(), request.getDescription());
        return ResponseEntity.ok(BrandMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar marca")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Marca eliminada")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        brandUseCases.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
