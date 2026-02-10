package com.dispositivos.catalog.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Tipo de dispositivo expuesto en el API")
public class DeviceTypeResponse {

    @Schema(description = "Identificador único del tipo", example = "1")
    private Long id;

    @Schema(description = "Nombre del tipo", example = "Smartphone")
    private String name;

    @Schema(description = "Descripción del tipo")
    private String description;
}
