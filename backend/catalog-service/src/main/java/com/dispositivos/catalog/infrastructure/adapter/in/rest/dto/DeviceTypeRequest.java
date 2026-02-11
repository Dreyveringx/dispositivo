package com.dispositivos.catalog.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@lombok.Data
@Schema(description = "Datos para crear o actualizar un tipo de dispositivo")
public class DeviceTypeRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    @Schema(description = "Nombre del tipo", example = "Smartphone", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Size(max = 500)
    @Schema(description = "Descripción opcional del tipo", example = "Teléfono inteligente")
    private String description;
}
