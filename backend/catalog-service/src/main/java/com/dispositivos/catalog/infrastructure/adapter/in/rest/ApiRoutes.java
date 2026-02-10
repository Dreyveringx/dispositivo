package com.dispositivos.catalog.infrastructure.adapter.in.rest;

/**
 * Rutas REST del API de catálogo. Centralizadas para mantenibilidad y posible versionado.
 */
public final class ApiRoutes {

    private ApiRoutes() {
    }

    public static final String API_BASE = "/api";
    public static final String BRANDS = API_BASE + "/brands";
    public static final String DEVICE_TYPES = API_BASE + "/device-types";
}
