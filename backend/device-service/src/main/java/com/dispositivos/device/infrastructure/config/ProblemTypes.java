package com.dispositivos.device.infrastructure.config;

import java.net.URI;

/**
 * URIs de tipos de problema RFC 7807. Centralizados para consistencia y mantenibilidad.
 * Uso: ProblemDetail.setType(ProblemTypes.NOT_FOUND)
 */
public final class ProblemTypes {

    private ProblemTypes() {
    }

    /** Recurso no encontrado (ej. 404) */
    public static final URI NOT_FOUND = URI.create("urn:problem-type:not-found");

    /** Error de validación de entrada (ej. 400 MethodArgumentNotValidException) */
    public static final URI VALIDATION_ERROR = URI.create("urn:problem-type:validation-error");

    /** Petición mal formada o parámetros inválidos (400 genérico) */
    public static final URI BAD_REQUEST = URI.create("urn:problem-type:bad-request");

    /** Conflicto con el estado actual del recurso (409) */
    public static final URI CONFLICT = URI.create("urn:problem-type:conflict");

    /** Error interno del servidor (500) */
    public static final URI INTERNAL_ERROR = URI.create("urn:problem-type:internal-error");
}
