package com.dispositivos.device.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@lombok.Data
@Schema(description = "Datos para crear o actualizar un dispositivo")
public class DeviceRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200)
    @Schema(description = "Nombre del dispositivo", example = "Galaxy S24", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Size(max = 2000)
    @Schema(description = "Descripción opcional del dispositivo")
    private String description;

    @NotNull(message = "El ID de marca es obligatorio")
    @Schema(description = "ID de la marca", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long brandId;

    @NotNull(message = "El ID de tipo de dispositivo es obligatorio")
    @Schema(description = "ID del tipo de dispositivo", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long deviceTypeId;

    @PastOrPresent(message = "La fecha de lanzamiento no puede ser futura")
    @Schema(description = "Fecha de lanzamiento", example = "2024-01-17")
    private LocalDate releaseDate;

    @Size(max = 500)
    @Schema(description = "URL de imagen principal")
    private String imageUrl;

    @Schema(description = "Lista de URLs de imágenes adicionales")
    private List<String> imageUrls;
}
