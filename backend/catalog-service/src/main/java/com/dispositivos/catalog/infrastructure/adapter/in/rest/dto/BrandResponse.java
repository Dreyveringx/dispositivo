package com.dispositivos.catalog.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Marca expuesta en el API")
public class BrandResponse {

    @Schema(description = "Identificador único de la marca", example = "1")
    private Long id;

    @Schema(description = "Nombre de la marca", example = "Samsung")
    private String name;

    @Schema(description = "Descripción de la marca")
    private String description;
}
