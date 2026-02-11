package com.dispositivos.catalog.infrastructure.config;

/**
 * Constantes para descripciones de @ApiResponse. Centraliza textos 400/404
 * y evita duplicación en controllers.
 */
public final class ApiDoc {

    private ApiDoc() {
    }

    public static final String ERROR_400 = "Datos de entrada inválidos (RFC 7807 Problem Detail)";
    public static final String ERROR_404_GENERIC = "Recurso no encontrado (RFC 7807 Problem Detail)";
    public static final String ERROR_404_BRAND = "Marca no encontrada (RFC 7807 Problem Detail)";
    public static final String ERROR_404_DEVICE_TYPE = "Tipo no encontrado (RFC 7807 Problem Detail)";
}
