package com.dispositivos.comment.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Comentario expuesto en el API")
public class CommentResponse {

    @Schema(description = "Identificador único del comentario", example = "1")
    private Long id;

    @Schema(description = "ID del dispositivo", example = "1")
    private Long deviceId;

    @Schema(description = "Autor del comentario", example = "Juan Pérez")
    private String author;

    @Schema(description = "Contenido del comentario")
    private String text;

    @Schema(description = "Fecha y hora de creación")
    private LocalDateTime createdAt;
}
