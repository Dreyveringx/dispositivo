package com.dispositivos.device.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Schema(description = "Dispositivo expuesto en el API")
public class DeviceResponse {

    @Schema(description = "Identificador único del dispositivo", example = "1")
    private Long id;

    @Schema(description = "Nombre del dispositivo", example = "Galaxy S24")
    private String name;

    @Schema(description = "Descripción del dispositivo")
    private String description;

    @Schema(description = "ID de la marca", example = "1")
    private Long brandId;

    @Schema(description = "ID del tipo de dispositivo", example = "1")
    private Long deviceTypeId;

    @Schema(description = "Fecha de lanzamiento", example = "2024-01-17")
    private LocalDate releaseDate;

    @Schema(description = "URL de imagen principal")
    private String imageUrl;

    @Schema(description = "Lista de URLs de imágenes adicionales")
    private List<String> imageUrls;
}
