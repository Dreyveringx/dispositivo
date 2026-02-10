package com.dispositivos.comment.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Datos para crear un comentario")
public class CommentRequest {

    @NotNull(message = "El ID del dispositivo es obligatorio")
    @Schema(description = "ID del dispositivo al que pertenece el comentario", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long deviceId;

    @NotBlank(message = "El autor es obligatorio")
    @Size(max = 200)
    @Schema(description = "Nombre o identificador del autor", example = "Juan Pérez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String author;

    @NotBlank(message = "El texto del comentario es obligatorio")
    @Size(max = 2000)
    @Schema(description = "Contenido del comentario", example = "Muy buen dispositivo.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String text;
}
